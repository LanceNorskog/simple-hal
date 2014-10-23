package us.norskog.simplehal.jersey;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.norskog.simplehal.SimpleHALInterceptorFilter;

public class SimpleTest extends JerseyTest {
	public static final String BASE = "http://localhost:9998/";
	private Builder request;

	@Override
	protected Application configure() {
		ResourceConfig resource = new ResourceConfig(HelloWorldResource.class);

		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put(
				ServerProperties.PROVIDER_PACKAGES,
				SimpleHALInterceptorFilter.class.getPackage().getName());
		resource.addProperties(initParams);
		return resource;
	}

	@Test
	public void smokeTest() {
		request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map value = request.get(Map.class);
		assertNotNull(value.get("_links"));
		Object object = value.get("_embedded");
		assertNull(object);
	}

	@Test
	public void linksTest() throws IOException {
		request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map value = request.get(Map.class);
		Object links = value.get("_links");
		links.hashCode();
		LinksHAL linksHAL = LinksHAL.unpack(links);
		for(Map<String, String> link: linksHAL) {
			if (link.get("rel").equals("self"))
				return;
		}
		assertTrue(false);
	}

	@Test
	public void embeddedTest() throws IOException {
		request = target("helloworld/embedded").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map value = request.get(Map.class);
		Object embedded = value.get("_embedded");
		embedded.hashCode();
		EmbeddedHAL embeddedHAL = EmbeddedHAL.unpack(embedded);
		System.out.println(embeddedHAL.keySet().toString());
		for(List<List<Map<String,String>>> outer: embeddedHAL.values()) {
			for(List<Map<String, String>> inner: outer) {
				for(Map<String, String> links: inner) {
					System.out.println(links.keySet().toString());
					if (links.containsKey("self"))
						return;
				}
			}
		}
		assertTrue(false);

	}
}

// secret classes to help me track the trees.

class LinksHAL extends ArrayList<Map<String, String>> {
	static private ObjectMapper mapper = new ObjectMapper();
	
	static LinksHAL unpack(Object ob) throws IOException {
		LinksHAL out = null;
		byte[] b;

		b = mapper.writeValueAsBytes(ob);
		out = mapper.readValue(b, LinksHAL.class);

		return out;
	}

}

class EmbeddedHAL extends HashMap<String, List<List<Map<String, String>>>> {
	static private ObjectMapper mapper = new ObjectMapper();
	
	static EmbeddedHAL unpack(Object ob) throws IOException {
		EmbeddedHAL out = null;
		byte[] b;

		b = mapper.writeValueAsBytes(ob);
		out = mapper.readValue(b, EmbeddedHAL.class);

		return out;
	}



}
