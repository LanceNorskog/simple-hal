package us.norskog.simplehal.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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