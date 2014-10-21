package us.norskog.simplehal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Jersey Filter/Interceptor for Simple-HAL links unpacker
 * For all requests for hal+json, add hyperlinks defined
 * by @Links annotation on endpoint.
 * Hyperlinks are defined with text titles, urls etc. which
 * include substitution strings which are filled in by Java EL
 * language.
 * 
 * Filter on the intake because only Filters get to see the base app's URI.
 * Interceptor on the outgo because only Interceptors get the data.
 * Oy!
 */

@Provider
@Links(linkset = @LinkSet(links = { @Link(href = "", rel = "") }))
public class SimpleHALInterceptorFilter implements WriterInterceptor, ContainerRequestFilter {
	public static final String HAL = "application/hal+json";

	static ThreadLocal<URI> baseURIs = new ThreadLocal<URI>(); 
	static ThreadLocal<Boolean> doAlls = new ThreadLocal<Boolean>(); 
	static Mapify mapifier = new Mapify();
	static Map<ParsedLinkSet, Evaluator> evaluators = new LinkedHashMap<ParsedLinkSet, Evaluator>();;

	public SimpleHALInterceptorFilter() {
		System.out.println("SimpleHALInterceptorFilter created");
	}

	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		URI baseUri = requestContext.getUriInfo().getBaseUri();
		baseURIs.set(baseUri);
		List<String> simplehal_json = requestContext.getUriInfo().getQueryParameters().get("simple-hal-json");
		doAlls.set(simplehal_json != null && simplehal_json.size() > 0 && simplehal_json.get(0).equals("true"));
	}

	//   @Override
	public void aroundWriteTo(WriterInterceptorContext context)
			throws IOException, WebApplicationException {
		try {
			Object entity = context.getEntity();
			Boolean boolean1 = doAlls.get();
			if ((!boolean1 && !context.getMediaType().toString().equals(HAL)) || entity == null) {
				context.proceed();
				return;
			}
			ParsedLinkSet parsedLinkSet = ParsedLinkSet.getParsedLinkSet(context.getAnnotations());
			if (parsedLinkSet == null) {
				context.proceed();
				return;
			}

			Map<String, Object> response = mapifier.convertToMap(entity);
			evaluate(parsedLinkSet, response);
			context.setEntity(response);
			context.proceed();
		} catch (RuntimeException e) {
			throw e;
		}
	}

	private void evaluate(ParsedLinkSet parsedLinkSet,
			Map<String, Object> response) {
		Evaluator evaluator = init(parsedLinkSet);
		List<Map<String, String>> expanded = evaluator.evaluateLinks(response);
		addBaseURI(expanded);
		if (expanded != null) {
			response.put("_links", expanded);
		}
		if (parsedLinkSet.getEmbeddedMap() != null) {
			Map<String, EmbeddedStore> storeMap = parsedLinkSet.getEmbeddedMap();
			Map<String, List<List<Map<String, String>>>> itemChunk = new LinkedHashMap<String, List<List<Map<String, String>>>>();
			for(String name: storeMap.keySet()) {
				EmbeddedStore store = storeMap.get(name);
				List<List<Map<String, String>>> embeddedLinks = new ArrayList<List<Map<String, String>>>();
				itemChunk.put(store.getName(), embeddedLinks);
				if (store.getPath() != null) {
					List<Map<String, String>> links = null;
					Object items = evaluator.evaluateExpr(store.getPath());
					if (items == null)
						continue;
					if (items.getClass().isArray()) {
						Object[] obs = (Object[]) items;
						for(int i = 0; i < obs.length; i++) {
							KV kv = new KV(Integer.toString(i), obs[i]);
							links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							embeddedLinks.add(links);
						}
					} else if (items instanceof Map) {
						for(Object key: ((Map<String,Object>) items).keySet()) {
							KV kv = new KV(key.toString(), ((Map<?, ?>) items).get(key.toString()));
							links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							embeddedLinks.add(links);						
						}
					} else if (items instanceof Collection) {
						int i = 0;
						for(Object ob: (Collection<?>) items) {
							KV kv = new KV(Integer.toString(i), ob);
							links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							embeddedLinks.add(links);		
							i++;
						}
					} else {
						KV kv = new KV("0", items);
						links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
						embeddedLinks.add(links);
					}
				}
				for(List<Map<String, String>> links: embeddedLinks) {
					addBaseURI(links);
				}
			}
			response.put("_embedded", itemChunk);
		}
	}

	private void addBaseURI(List<Map<String, String>> links) {
		String baseURI = baseURIs.get().toString();
		if (baseURI.endsWith("/"))
			baseURI = baseURI.substring(0, baseURI.length() - 1);
		for(Map<String,String> link: links) {
			String href = link.get("href");
			if (href != null) {
				if (href.startsWith("/"))
					href = href.substring(1);
				link.put("href", baseURI + "/" + href);
			}
		}
	}

	private Evaluator init(ParsedLinkSet parsedLinkSet) {
		if (! evaluators.containsKey(parsedLinkSet)) {
			// idempotent race condition v.s. synchronized in every call 
			synchronized(SimpleHALInterceptorFilter.class) {
				Evaluator evaluator = new Evaluator(parsedLinkSet);
				evaluators.put(parsedLinkSet, evaluator);
			}
		}
		return evaluators.get(parsedLinkSet);
	}

	void dumpContext(WriterInterceptorContext context) {
		System.out.println("getType: " + context.getType().getClass());
		System.out.println("getEntity: " + context.getEntity().getClass());
		System.out.println("getGenericType: " + context.getGenericType().getTypeName());
		System.out.println("getMediaType: " + context.getMediaType().toString());
		Annotation[] annos = context.getAnnotations();
		for(Annotation anno: annos) {
			System.out.println("\tanno: " + anno.toString());
		}
	}

}
