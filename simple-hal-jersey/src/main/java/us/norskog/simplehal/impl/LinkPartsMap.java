package us.norskog.simplehal.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * _links: ["name":{...
 */

@SuppressWarnings("serial")
public class LinkPartsMap extends HashMap<String,String>{

	public LinkPartsMap() {;}
	public LinkPartsMap(Map<String,String> map) {
		for(String key: map.keySet())
			this.put(key, map.get(key));
	}
}
