package us.norskog.simplehal.impl;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import us.norskog.simplehal.Supplier;
import us.norskog.simplehal.Items;
import us.norskog.simplehal.Link;
import us.norskog.simplehal._Embedded;
import us.norskog.simplehal._Links;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Jersey Filter/Interceptor for SimplHAL links unpacker
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
 * TODO: add support for external additions to endpoints.
 *   it might add to this, this might pull from it.
 *   haven't decided.
 *   
 * TODO: maybe can just give entire link with multiple ELs?
 *   ParsedLinkSet is maybe overkill?
 *   
 * TODO: add path/url/this/params to EL?
 *   Definitely add full base to curies
 * 
 * TODO: change URI from threadlocal to retained property because 
 *    jersey is goofy about threads.
 */

@Provider
@_Links(linkset = Supplier.class)
@_Embedded(linkset = Supplier.class)
public class SimpleHALInterceptorFilter implements WriterInterceptor, ContainerRequestFilter {
	public static final String HAL = "application/hal+json";

	static ThreadLocal<UriInfo> baseURIs = new ThreadLocal<UriInfo>(); 
	static Mapify mapifier = new Mapify();
	static Builder builder = new Builder();
	static Formatter formatter = new SimpleFormatter();
	static Map<ParsedLinkSet, Evaluator> evaluators = new HashMap<ParsedLinkSet, Evaluator>();;

	public SimpleHALInterceptorFilter() {
	}

	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		UriInfo baseUri = requestContext.getUriInfo();
		baseURIs.set(baseUri);
		URI abs = baseUri.getAbsolutePath();
		URI request = baseUri.getRequestUri();
		requestContext.setProperty("baseURI", baseUri.getBaseUri().toString());
		requestContext.setProperty("absURI", abs.toString());
		requestContext.setProperty("requestURI", request.toString());
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
			if (response == null) {
				throw new WebApplicationException("SimpleHAL: response class must be POJO or Map, not " + entity.getClass().getCanonicalName());
			}
			String path = getPath(context.getAnnotations());
			LinksetMap builtLinks = builder.buildLinks(parsedLinkSet, evaluator, path, response);
			// TODO: add curies for base path
			String selfLink = getSelf((String) context.getProperty("baseURI"), (String) context.getProperty("requestURI"));
			if (! builtLinks.containsKey("self"))
				builder.addLink(builtLinks, "self", selfLink);
			EmbeddedMap builtEmbedded = builder.buildEmbedded(parsedLinkSet, evaluator, path, response);
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

	// path of class, not method
	 String getPath(Annotation[] annos) {
		String part = "";
		for(Annotation anno: annos) {
			if (anno instanceof Path)
				part = ((Path) anno).value();
		}
		String path = baseURIs.get().getPath();
		path = path.substring(0, path.length() - part.length());
		if (path.endsWith("/"))
			path = path.substring(0, path.length() - 1);
		if (! path.startsWith("/"))
			path = "/" + path;
		return path;
	}
	 
	String getSelf(String base, String full) {
		String self = full.substring(base.length());
		if (! self.startsWith("/"))
			self = "/" + self;
		while (self.endsWith("/"))
			self = self.substring(0, self.length() - 1);
		return self;
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
