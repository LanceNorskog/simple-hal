package us.norskog.simplehal.jersey;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.norskog.simplehal.SimpleHALInterceptorFilter;

/**
 * Start Jersey app at 9998.
 * 
 * @author lance
 *
 */

public class SimpleTest extends JerseyTest {
	public static final String BASE = "http://localhost:9998/";

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

	/*	
    For some seekrit reason, this is totally buggered in the jersey test harness. Like, totally.
	 */
	@Test
	public void nullTest() {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/links").request("application/json");
		final Map unpacked = request.get(Map.class);
		assertNull(unpacked.get("_links"));
		assertNull(unpacked.get("_embedded"));
	}

	@Test
	public void smokeTest() {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map unpacked = request.get(Map.class);
		assertNotNull(unpacked.get("_links"));
		Object object = unpacked.get("_embedded");
		assertNull(object);
	}

	@Test
	public void linksTest() throws IOException {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map unpacked = request.get(Map.class);
		Object links = unpacked.get("_links");
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
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/embedded").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map unpacked = request.get(Map.class);
		Object embedded = unpacked.get("_embedded");
		embedded.hashCode();
		EmbeddedHAL embeddedHAL = EmbeddedHAL.unpack(embedded);
		System.out.println(embeddedHAL.keySet().toString());
		for(List<List<Map<String,String>>> outer: embeddedHAL.values()) {
			for(List<Map<String, String>> inner: outer) {
				for(Map<String, String> links: inner) {
					System.out.println(links.keySet().toString());
					if (links.containsKey("rel"))
						return;
				}
			}
		}
		assertTrue(false);
	}

	@Test
	public void checkLinksTest() throws IOException {
		Value local = new Value();
		HelloWorldResource.setValue(local);
		local.setDoFirst(false);
		Builder request = target("helloworld/check").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map unpacked = request.get(Map.class);
		Object linkSet = unpacked.get("_links");
		linkSet.hashCode();
		LinksHAL linksHal = LinksHAL.unpack(linkSet);
		System.out.println("link to check: " + linksHal.toString());
		for(Map<String, String> links: linksHal) {
			System.out.println("First links: " + links.keySet().toString());
			if (links.get("rel").equals("first"))
				assertTrue(false);
		}
	}

	@Test
	public void checkEmbeddedTest() throws IOException {
		Value local = new Value();
		HelloWorldResource.setValue(local);
		local.setDoFirst(false);
		local.setDoArray(false);
		local.setDoList(false);
		local.setDoMap(true);
		Builder request = target("helloworld/check").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map unpacked = request.get(Map.class);
		Object embedded = unpacked.get("_embedded");
		embedded.hashCode();
		EmbeddedHAL embeddedHAL = EmbeddedHAL.unpack(embedded);
		System.out.println(embeddedHAL.keySet().toString());
		assertTrue(embeddedHAL.keySet().contains("Mappacious"));
		for(List<List<Map<String,String>>> outer: embeddedHAL.values()) {
			for(List<Map<String, String>> inner: outer) {
				for(Map<String, String> links: inner) {
					System.out.println("First links: " + links.keySet().toString());
					if (links.get("rel").equals("first"))
						assertTrue(false);
				}
			}
		}
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
