package us.norskog.simplehal;

import java.util.HashMap;
import java.util.Map;

public class LinksetMap extends HashMap<String, LinkPartsMap> {

		public void setMap(Map<String, Map<String, String>> expanded) {
		for(String key: expanded.keySet()) {
			LinkPartsMap partsMap = new LinkPartsMap();
			Map<String, String> map = expanded.get(key);
			for(String key2: map.keySet())
				partsMap.put(key2, map.get(key2));
			this.put(key, partsMap);
		}
	}

}
