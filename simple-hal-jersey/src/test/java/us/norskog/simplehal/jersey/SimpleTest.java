package us.norskog.simplehal.jersey;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import us.norskog.simplehal.SimpleHALInterceptorFilter;

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
	    public void smokeTest() {
//	    	target("helloworld/links").queryParam("simple_json", "true")
	        Builder request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
			final Value value = request.get(Value.class);
	        assertNotNull(value.get_links());
	        assertNull(value.get_embedded());
	    }
		 
	    @Test
	    public void urlTest() {
	        Builder request = target("helloworld/links").queryParam("simple-hal-json", "true").request(SimpleHALInterceptorFilter.HAL);
			final Value value = request.get(Value.class);
	        assertNotNull(value.get_links());
	        assertNull(value.get_embedded());
	    }
}
