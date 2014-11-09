package us.norskog.simplehal.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.norskog.simplehal.impl.SimpleHALInterceptorFilter;

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

	@Test
	public void noTest() {
		assertTrue(true);
		System.out.println("noTest");
	}

	@Test
	public void topTest() {
		System.out.println("topTest");
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld").request("text/plain");
		String response = request.get(String.class);
		assertTrue(response.startsWith("SimpleHAL"));
	}

	@Test(expected=WebApplicationException.class)
	public void arrayTest() {
		System.out.println("arrayTest");
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/array").request(SimpleHALInterceptorFilter.HAL);
		String response = request.get(String.class);
		assertFalse(true);
	}

	/*	
    For some seekrit reason, this is totally buggered in the jersey test harness. Like, totally.
	 */
	@Test
	public void nullTest() {
		System.out.println("nullTest");
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/links").request("application/json");
		final Map<String,Object> unpacked = request.get(Map.class);
		assertNull(unpacked.get("_links"));
		assertNull(unpacked.get("_embedded"));
	}

	@Test
	public void smokeTest() {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map<String,Object> unpacked = request.get(Map.class);
		assertNotNull(unpacked.get("_links"));
		Object object = unpacked.get("_embedded");
		assertNull(object);
	}

	@Test
	public void pathTest() throws IOException {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map<String,Object> unpacked = request.get(Map.class);
		Map links = (Map) unpacked.get("_links");
		Map<String,Object> linkSet = (Map<String, Object>) unpacked.get("_links");
		LinksHAL linksHal = LinksHAL.unpack(linkSet);
		String url = linksHal.get("self").get("href");
		System.out.println(url);
		assertTrue(url.equals("/helloworld/links"));
		assertFalse(url.contains("localhost"));
	}

	@Test
	public void linksTest() throws IOException {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map<String,Object> unpacked = request.get(Map.class);
		Map links = (Map) unpacked.get("_links");
		Map<String,Object> linkSet = (Map<String, Object>) unpacked.get("_links");
		LinksHAL linksHal = LinksHAL.unpack(linkSet);
		String url = linksHal.get("self").get("href");
		assertNotNull(url);
	}

	@Test
	public void embeddedTest() throws IOException {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/embedded").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map unpacked = request.get(Map.class);
		//		printJson(unpacked);
		Map<String,Object> embedded = (Map) unpacked.get("_embedded");
		EmbeddedHAL embeddedHAL = EmbeddedHAL.unpack(embedded);
		for(List<Map<String, Map<String, Map<String, String>>>> outer: embeddedHAL.values()) {
			for(Map<String, Map<String, Map<String, String>>> inner: outer) {
				for(Map<String,Map<String, String>> links: inner.values()) {
					for(Map<String, String> parts: links.values()) {
						if (parts.containsKey("href"))
							return;
					}
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
		final Map<String,Object> unpacked = request.get(Map.class);
		Map<String,Object> linkSet = (Map<String, Object>) unpacked.get("_links");
		LinksHAL linksHal = LinksHAL.unpack(linkSet);
		assertFalse(linksHal.keySet().contains("first"));
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
		final Map<String,Object> unpacked = request.get(Map.class);
		Map<String,Object> embedded = (Map<String, Object>) unpacked.get("_embedded");
		EmbeddedHAL embeddedHAL = EmbeddedHAL.unpack(embedded);
		assertTrue(embeddedHAL.get("Mappacious").size() > 0);
		assertFalse(embeddedHAL.get("Arraysious").size() > 0);
		assertFalse(embeddedHAL.get("Listicle").size() > 0);
		for(List<Map<String, Map<String, Map<String, String>>>> outer: embeddedHAL.values()) {
			for(Map<String, Map<String, Map<String, String>>> inner: outer) {
				for(Map<String,Map<String, String>> links: inner.values()) {
					if (links.containsKey("first"))
						assertTrue(false);
				}
			}
		}
	}

	// verify that created object is valid json.
	private void printJson(Map unpacked) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			byte[] b = mapper.writeValueAsBytes(unpacked);
			System.out.println( new String(b));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

// secret classes to help me track the trees.

class LinksHAL extends HashMap< String, Map<String, String>> {
	static private ObjectMapper mapper = new ObjectMapper();

	static LinksHAL unpack(Map ob) throws IOException {
		LinksHAL out = null;
		byte[] b;

		b = mapper.writeValueAsBytes(ob);
		out = mapper.readValue(b, LinksHAL.class);

		return out;
	}

}

class EmbeddedHAL extends HashMap<String,List<Map<String,Map<String,Map<String,String>>>>> {
	static private ObjectMapper mapper = new ObjectMapper();

	static EmbeddedHAL unpack(Map ob) throws IOException {
		Map<String,Object> ob2 = ob;
//		for(String key: ob2.keySet())
//			System.out.println("_embedded: " + key);
		EmbeddedHAL out = null;
		byte[] b;

		b = mapper.writeValueAsBytes(ob);
		out = mapper.readValue(b, EmbeddedHAL.class);

		return out;
	}



}
