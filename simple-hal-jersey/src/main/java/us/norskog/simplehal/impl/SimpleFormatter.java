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




	//	@Override
	//	public Map<String, Object> format(Map<String, Object> response,
	//			List<Map<String, String>> builtLinks,
	//			Map<String, List<Map<String,Map<String, String>>>> builtEmbedded) {
	//		Map<String, Object> formatted = new HashMap<String, Object>();
	//		formatted.put("_links", builtLinks);
	//		
	//		if (builtEmbedded != null) 
	//			formatted.put("_embedded", builtEmbedded);
	//		return formatted;
	//	}

	//	private Map<String, List<Map<String,Map<String, String>>>> 
	//	reformat(
	//			Map<String, List<List<Map<String, String>>>> builtEmbedded) {
	//		Map<String, List<Map<String,Map<String, String>>>> out = new HashMap<String, List<Map<String,Map<String,String>>>>();
	//		for(String key: builtEmbedded.keySet()) {
	//			List<Map<String,Map<String,String>>> list1 = new ArrayList<Map<String,Map<String,String>>>();
	//			
	//		}
	//		
	//		
	//		return null;
	//	}

}
