package us.norskog.simplehal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFormatter implements Formatter {

	public Map<String, Object> format(
			Map<String, Object> response,
			Map<String, Map<String, String>> builtLinks,
			Map<String, Map<String, List<Map<String, Map<String, String>>>>> builtEmbedded) {
		Map<String, Object> formatted = new HashMap<String, Object>();
		formatted.put("_links", builtLinks);
		
		if (builtEmbedded != null) 
			formatted.put("_embedded", builtEmbedded);
		return formatted;
	}

	public Map<String, Object> format(Map<String, Object> response,
			LinksetMap builtLinks, Map<String, EmbeddedMap> builtEmbedded) {
		response.put("_links", builtLinks);
		
		if (builtEmbedded != null) 
			response.put("_embedded", builtEmbedded);
		return response;
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
