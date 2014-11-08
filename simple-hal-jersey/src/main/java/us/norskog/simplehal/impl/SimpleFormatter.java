package us.norskog.simplehal.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Formatter for IETF draft format.
 *
 */
public class SimpleFormatter implements Formatter {

	public Map<String, Object> format(Map<String, Object> response,
			LinksetMap builtLinks, EmbeddedMap builtEmbedded) {
		response.put("_links", builtLinks);
		// transform embedded to add "_links" to LinksetList for each map inside
		if (builtEmbedded != null) {
			Map<String,Object> embedded = expandEmbedded(builtEmbedded);
			response.put("_embedded", embedded);
		}
		return response;
	}

	private Map<String,Object> expandEmbedded(EmbeddedMap builtEmbedded) {
		Map<String, Object> full = new HashMap<String, Object>();
		for(String key: builtEmbedded.keySet()) {
			LinksetList list = builtEmbedded.get(key);
			List<Map<String,LinksetMap>> expand = new ArrayList<Map<String,LinksetMap>>();
			full.put(key, expand);
			if (list == null)
				this.hashCode();
			for(LinksetMap map: list) {
				Map<String,LinksetMap> stub = new HashMap<String, LinksetMap>();
				stub.put("_links", map);
				expand.add(stub);
			}
		}
		return full;
	}

}
