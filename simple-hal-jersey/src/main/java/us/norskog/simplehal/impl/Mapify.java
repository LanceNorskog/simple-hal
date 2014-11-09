package us.norskog.simplehal.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * Convert arbitrary object into map.
 *  Uses Jackson. Should use something else or just custom thing for reflection.
 * 
 */

public class Mapify {
	private static final Map<String,Object> mapclass = new HashMap<String, Object>();
	// It takes 1-2 ms to create an ObjectMapper
	private static ThreadLocal<ObjectMapper> mappers = new ThreadLocal<ObjectMapper>();

	@SuppressWarnings("unchecked")
	public Map<String, Object> convertToMap(Object obj) {
		if (obj instanceof Number || obj instanceof String)
			return null;
		if (obj instanceof Collection<?> || obj.getClass().isArray())
			return null;
		Map<String, Object> objectAsMap;
		try {
			byte[] b;
			if (mappers.get() == null)
				mappers.set(new ObjectMapper());
			b = mappers.get().writeValueAsBytes(obj);
			objectAsMap = (Map<String,Object>) mappers.get().readValue(b, mapclass.getClass());
			return objectAsMap;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	public Object[] convertToArray(Object[] objArray)  {	
		Object[] asArray = new Object[objArray.length];
		for(int i = 0; i < objArray.length; i++) {
			asArray[i] = convertToMap(objArray[i]);
		}
		return asArray;
	}

	//	public Map getMap(Object obj) {
	//
	//		ObjectMapper om = new ObjectMapper();
	//		Map<String, Object> objectAsMap = om.convertValue(obj, Map.class);
	//		return objectAsMap;
	//	}

	//	public Object convert_object(Object obj) throws IntrospectionException,
	//	IllegalAccessException, IllegalArgumentException,
	//	InvocationTargetException {
	//		if (obj instanceof String || obj instanceof Number)
	//			return obj;
	//		else if (obj.getClass().isArray()) {
	//			Object[] value = new Object
	//		}
	//			
	//	}
	//	
	//	public Object convert_object_to_map_java(Object obj) throws IntrospectionException,
	//	IllegalAccessException, IllegalArgumentException,
	//	InvocationTargetException {
	//		if (obj instanceof String || obj instanceof Number)
	//			return obj;
	//		else if (obj.getClass().isArray()) {
	//			
	//		}
	//			
	//		Map<String, Object> objectAsMap = new HashMap<String, Object>();
	//		BeanInfo info = Introspector.getBeanInfo(obj.getClass());
	//		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	//			Method reader = pd.getReadMethod();
	//			if (reader != null && reader.getName().startsWith("get") || reader.getName().startsWith("is"))
	//			{
	//				objectAsMap.put(pd.getName(), (reader.invoke(obj)));
	//			}
	//		}
	//		return objectAsMap;				
	//	}
	//	
	//	public Object convert_object_to_map_java(Object obj) throws IntrospectionException,
	//	IllegalAccessException, IllegalArgumentException,
	//	InvocationTargetException {
	//		if (obj instanceof String || obj instanceof Number)
	//			return obj;
	//		else if (obj.getClass().isArray()) {
	//			
	//		}
	//			
	//		Map<String, Object> objectAsMap = new HashMap<String, Object>();
	//		BeanInfo info = Introspector.getBeanInfo(obj.getClass());
	//		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	//			Method reader = pd.getReadMethod();
	//			if (reader != null && reader.getName().startsWith("get") || reader.getName().startsWith("is"))
	//			{
	//				objectAsMap.put(pd.getName(), (reader.invoke(obj)));
	//			}
	//		}
	//		return objectAsMap;				
	//	}

}