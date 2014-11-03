//package org.glassfish.jersey.examples.helloworld.webapp;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.StringWriter;
//import java.util.Arrays;
//
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.ext.Provider;
//import javax.ws.rs.ext.WriterInterceptor;
//import javax.ws.rs.ext.WriterInterceptorContext;
//
//@Provider
//@Links
//public class SimpleInterceptor implements WriterInterceptor {
//
//	public SimpleInterceptor() {
//		System.err.println("SimpleInterceptor created");
//	}
//	
//	@Override
//	public void aroundWriteTo(WriterInterceptorContext context)
//			throws IOException, WebApplicationException {
//		// TODO Auto-generated method stub
//		System.err.println("SimpleInterceptor called");
//		Object ob = context.getEntity();
//		if (ob == null)
//			System.err.println("\tEntity object is null!");
//		else		
//			System.err.println("\tEntity type: " + ob.getClass().getCanonicalName().toString());
//		OutputStream os = context.getOutputStream();
//		
//		byte[] buf = new byte[100];
//		Arrays.fill(buf, (byte) 'b'); 
//		os.write(buf);
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		context.setOutputStream(bos);
//		context.proceed();
//	}
//
//}
