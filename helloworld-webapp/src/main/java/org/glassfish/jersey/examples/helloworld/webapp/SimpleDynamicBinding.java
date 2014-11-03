package org.glassfish.jersey.examples.helloworld.webapp;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

//This dynamic binding provider registers GZIPWriterInterceptor
//only for HelloWorldResource and methods that contain
//"VeryLongString" in their name. It will be executed during
//application initialization phase.
//public class SimpleDynamicBinding implements DynamicFeature {
//
//	public SimpleDynamicBinding() {
//   	 System.err.println("SimpleDynamicBinding.constructor");
//
//	}
//	
// @Override
// public void configure(ResourceInfo resourceInfo, FeatureContext context) {
//	 System.err.println("SimpleDynamicBinding.configure");
//     if (true) {
//         context.register(SimpleInterceptor.class);
//     }
// }
//}

