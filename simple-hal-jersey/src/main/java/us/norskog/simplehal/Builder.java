package us.norskog.simplehal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Build return links and embedded trees from parsed annotations, return object.
 *
 */

public class Builder {

	public List<Map<String, String>> buildLinks(ParsedLinkSet parsedLinkSet,
			Evaluator evaluator, Map<String, Object> response) {
		List<Map<String, String>> expanded = evaluator.evaluateLinks(response);
		return expanded;
	}

	public Map<String, List<List<Map<String, String>>>> buildEmbedded(ParsedLinkSet parsedLinkSet,
			Evaluator evaluator, Map<String, Object> response) {
		if (parsedLinkSet.getEmbeddedMap() != null) {
			Map<String, EmbeddedStore> storeMap = parsedLinkSet.getEmbeddedMap();
			Map<String, List<List<Map<String, String>>>> itemChunk = new LinkedHashMap<String, List<List<Map<String, String>>>>();
			for(String name: storeMap.keySet()) {
				EmbeddedStore store = storeMap.get(name);
				List<List<Map<String, String>>> embeddedLinks = new ArrayList<List<Map<String, String>>>();
				itemChunk.put(store.getName(), embeddedLinks);
				if (store.getPath() != null) {
					List<Map<String, String>> links = null;
					Object items = evaluator.evaluateExpr(store.getPath());
					if (items == null)
						continue;
					if (items.getClass().isArray()) {
						Object[] obs = (Object[]) items;
						for(int i = 0; i < obs.length; i++) {
							KV kv = new KV(Integer.toString(i), obs[i]);
							links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							if (links != null)
								embeddedLinks.add(links);						
						}
					} else if (items instanceof Map) {
						for(Object key: ((Map<String,Object>) items).keySet()) {
							KV kv = new KV(key.toString(), ((Map<?, ?>) items).get(key.toString()));
							links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							if (links != null)
								embeddedLinks.add(links);						
						}
					} else if (items instanceof Collection) {
						int i = 0;
						for(Object ob: (Collection<?>) items) {
							KV kv = new KV(Integer.toString(i), ob);
							links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							if (links != null)
								embeddedLinks.add(links);						
							i++;
						}
					} else {
						KV kv = new KV("0", items);
						links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
						if (links != null)
							embeddedLinks.add(links);						
					}
				}
			}
			return itemChunk;
		}
		return null;
	}
	
}
