package us.norskog.simplehal.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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