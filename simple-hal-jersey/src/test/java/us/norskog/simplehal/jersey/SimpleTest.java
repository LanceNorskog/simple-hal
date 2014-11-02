package us.norskog.simplehal.jersey;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
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
		final Map<String,Map> unpacked = request.get(Map.class);
		Map links = unpacked.get("_links");
		printJson(links);
	}

	@Test
	public void embeddedTest() throws IOException {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/embedded").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map unpacked = request.get(Map.class);
		//		printJson(unpacked);
		Map embedded = (Map) unpacked.get("_embedded");
		printJson(embedded);
		System.out.println(embedded.toString());
		EmbeddedHAL embeddedHAL = EmbeddedHAL.unpack(embedded);
		embedded.hashCode();
		//		for(List<List<Map<String,String>>> outer: embeddedHAL.values()) {
		//			for(List<Map<String, String>> inner: outer) {
		//				for(Map<String, String> links: inner) {
		//					if (links.containsKey("rel"))
		//						return;
		//				}
		//			}
		//		}
		//		assertTrue(false);
	}

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

	@Test
	public void checkLinksTest() throws IOException {
		Value local = new Value();
		HelloWorldResource.setValue(local);
		local.setDoFirst(false);
		Builder request = target("helloworld/check").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map<String,Map> unpacked = request.get(Map.class);
		Map linkSet = unpacked.get("_links");
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
		Map embedded = (Map) unpacked.get("_embedded");
		EmbeddedHAL embeddedHAL = EmbeddedHAL.unpack(embedded);
		assertTrue(embeddedHAL.keySet().contains("Mappacious"));
		assertFalse(embeddedHAL.keySet().contains("Arraysious"));
		assertFalse(embeddedHAL.keySet().contains("Listicle"));
		//		Collection<Map<String, List<Map<String, Map<String, String>>>>> outer = embeddedHAL.values();
		for(Map<String, List<Map<String, Map<String, String>>>> outer: embeddedHAL.values()) {
			for(List<Map<String, Map<String, String>>> inner: outer.values()) {
				for(Map<String,Map<String, String>> links: inner) {
					for(Map<String, String> parts: links.values()) {
						if (parts.get("rel").equals("first"))
							assertTrue(false);
					}
				}
			}
			hashCode();
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
		Map x = mapper.readValue(b, Map.class);
		out = mapper.readValue(b, LinksHAL.class);

		return out;
	}

}

class EmbeddedHAL extends HashMap<String,Map<String,List<Map<String,Map<String,String>>>>> {
	static private ObjectMapper mapper = new ObjectMapper();

	static EmbeddedHAL unpack(Map ob) throws IOException {
		Map<String,Object> ob2 = ob;
		for(String key: ob2.keySet())
			System.out.println("_embedded: " + key);
		EmbeddedHAL out = null;
		byte[] b;

		b = mapper.writeValueAsBytes(ob);
		out = mapper.readValue(b, EmbeddedHAL.class);

		return out;
	}



}
