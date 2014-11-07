package us.norskog.simplehal.impl;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import us.norskog.simplehal.Items;
import us.norskog.simplehal.Link;
import us.norskog.simplehal.LinkSet;
import us.norskog.simplehal._Embedded;
import us.norskog.simplehal._Links;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
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
 * 
 */

@Provider
@_Links(linkset = @LinkSet(links = { @Link(href = "", rel = "") }))
@_Embedded(value = { @Items(items = "", links = @LinkSet(links = { @Link(href = "", rel = "") }), name = "") })
public class SimpleHALInterceptorFilter implements WriterInterceptor, ContainerRequestFilter {
	public static final String HAL = "application/hal+json";

	static ThreadLocal<URI> baseURIs = new ThreadLocal<URI>(); 
	static Mapify mapifier = new Mapify();
	static Builder builder = new Builder();
	static Formatter formatter = new SimpleFormatter();
	static Map<ParsedLinkSet, Evaluator> evaluators = new HashMap<ParsedLinkSet, Evaluator>();;

	public SimpleHALInterceptorFilter() {
	}

	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		URI baseUri = requestContext.getUriInfo().getBaseUri();
		baseURIs.set(baseUri);
	}

	//   @Override
	public void aroundWriteTo(WriterInterceptorContext context)
			throws IOException, WebApplicationException {

		try {
			Object entity = context.getEntity();
			if (!context.getMediaType().toString().equals(HAL) || entity == null) {
				context.proceed();
				return;
			}
			ParsedLinkSet parsedLinkSet = ParsedLinkSet.getParsedLinkSet(context.getAnnotations());
			if (parsedLinkSet == null) {
				context.proceed();
				return;
			}

			Evaluator evaluator = init(parsedLinkSet);
			Map<String, Object> response = mapifier.convertToMap(entity);
			LinksetMap builtLinks = builder.buildLinks(parsedLinkSet, evaluator, response);
			addBaseURI(builtLinks);
			EmbeddedMap builtEmbedded = builder.buildEmbedded(parsedLinkSet, evaluator, response);
			if (builtEmbedded != null) {
				for(LinksetList embedded: builtEmbedded.values()) {
					for(LinksetMap ls: embedded) {
						addBaseURI(ls);
					}
				}
			}
			Map<String, Object> formatted = formatter.format(response, builtLinks, builtEmbedded);
			context.setEntity(formatted);
			context.proceed();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		} finally {
			baseURIs.set(null);
		}
	}

	private void addBaseURI(LinksetMap builtLinks) {
		String baseURI = getBaseURI();
		for(String key: builtLinks.keySet()) {
			LinkPartsMap linkMap = builtLinks.get(key);
			String href = linkMap.get("href");
			if (href != null) {
				if (href.startsWith("/"))
					href = href.substring(1);
				linkMap.put("href", baseURI + "/" + href);
			}
		}
	}

	private String getBaseURI() {
		String baseURI = baseURIs.get().toString();
		if (baseURI.endsWith("/"))
			baseURI = baseURI.substring(0, baseURI.length() - 1);
		return baseURI;
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

}