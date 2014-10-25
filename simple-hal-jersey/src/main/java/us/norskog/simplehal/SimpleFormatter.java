package us.norskog.simplehal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFormatter implements Formatter {

	public Map<String, Object> format(Map<String, Object> response,
			List<Map<String, String>> builtLinks,
			Map<String, List<List<Map<String, String>>>> builtEmbedded) {
		Map<String, Object> formatted = new HashMap<String, Object>();
		formatted.put("_links", builtLinks);
		if (builtEmbedded != null) 
			formatted.put("_embedded", builtEmbedded);
		return formatted;
	}

}
