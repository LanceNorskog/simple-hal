package us.norskog.simplehal.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerFactory;
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

public class Simple2Test extends JerseyTest {
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
	public void hyperTest() {
		HelloWorldResource.setValue(new Value());
		Builder request = target("helloworld/hyper").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
		final Map<String,Object> unpacked = request.get(Map.class);
		assertNotNull(unpacked.get("_links"));
		assertNotNull(unpacked.get("_embedded"));
	}
	
	protected org.glassfish.jersey.test.spi.TestContainerFactory getTestContainerFactory() 
			throws org.glassfish.jersey.test.spi.TestContainerException {
		return new myfac(super.getTestContainerFactory(), "simplehal");
	};

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
	
	class myfac implements TestContainerFactory {
		TestContainerFactory fac;
		String base;
		
		myfac(TestContainerFactory fac, String base) {
			this.fac = fac;
			this.base = base;
		}

		public TestContainer create(URI baseUri,
				DeploymentContext deploymentContext) {
			if (fac instanceof myfac)
				return fac.create(baseUri, deploymentContext);
			try {
				URI uri = new URI(baseUri.toString() + base);
				System.out.println("base uri: " + baseUri.toString());
				System.out.println("full uri: " + uri.toString());
				return fac.create(uri, deploymentContext);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}

}