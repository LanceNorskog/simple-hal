package us.norskog.simplehal.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Build return links and embedded trees from parsed annotations, return object.
 *
 */

public class Builder {

	public LinksetMap buildLinks(ParsedLinkSet parsedLinkSet,
			Evaluator evaluator, String path, Map<String, Object> response) {
		Map<String, Map<String, String>> expanded = evaluator.evaluateLinks(path, response);
		LinksetMap lsm = new LinksetMap(expanded);
		return lsm;
	}

	public EmbeddedMap buildEmbedded(ParsedLinkSet parsedLinkSet,
			Evaluator evaluator, String path, Map<String, Object> response) {
		if (parsedLinkSet.getEmbeddedMap() != null) {
			List<ItemStore> storeList = parsedLinkSet.getEmbeddedMap();
			EmbeddedMap _embedded = new EmbeddedMap();
			for(ItemStore store: storeList) {
				EmbeddedMap _links = new EmbeddedMap();
				LinksetList embeddedLinks = new LinksetList();
				_embedded.put(store.getName(), embeddedLinks);
				if (store.getPath() != null) {
					Object items = evaluator.evaluateExpr(store.getPath());
					if (items == null)
						continue;
					LinksetMap linkset;
					if (items.getClass().isArray()) {
						Object[] obs = (Object[]) items;
						for(int i = 0; i < obs.length; i++) {
							KV kv = new KV(Integer.toString(i), obs[i]);
							linkset = new LinksetMap(evaluator.evaluateEmbeddedItem(store.getName(), path, response, kv));
							if (linkset.size() > 0)
								embeddedLinks.add(linkset);	
							mapify(linkset);
						}
					} else if (items instanceof Map) {
						for(Object key: ((Map<String,Object>) items).keySet()) {
							KV kv = new KV(key.toString(), ((Map<?, ?>) items).get(key.toString()));
							linkset = new LinksetMap(evaluator.evaluateEmbeddedItem(store.getName(), path, response, kv));
							if (linkset.size() > 0)
								embeddedLinks.add(linkset);	
							mapify(linkset);

						}
					} else if (items instanceof Collection) {
						int i = 0;
						for(Object ob: (Collection<?>) items) {
							KV kv = new KV(Integer.toString(i), ob);
							linkset = new LinksetMap(evaluator.evaluateEmbeddedItem(store.getName(), path, response, kv));
							if (linkset.size() > 0)
								embeddedLinks.add(linkset);	
							mapify(linkset);

							i++;
						}
					} else {
						KV kv = new KV("0", items);
						linkset = new LinksetMap(evaluator.evaluateEmbeddedItem(store.getName(), path, response, kv));
						if (linkset.size() > 0)
							embeddedLinks.add(linkset);	
						mapify(linkset);
					}
				}
			}
			mapify(_embedded);
			return _embedded;
		}
		return null;
	}
	
//	private LinksetMap linkify(
//			Map<String, Map<String, String>> evaluateEmbeddedItem) {
//		LinksetMap lsm = new LinksetMap();
//		lsm.setMap(evaluateEmbeddedItem);
//		return lsm;
//	}

	static ObjectMapper mapper = new ObjectMapper();

	void mapify(Map map) {
		if (map == null)
			return;
		byte[] b;
		try {
			b = mapper.writeValueAsBytes(map);
			Map objectAsMap = mapper.readValue(b, Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void addLink(LinksetMap builtLinks, String name, String link) {
		HashMap<String, String> add = new HashMap<String, String>();
		add.put("href", link);
		builtLinks.put(name, new LinkPartsMap(add));
	}

}

// item.key, item.value
class KV {
	private String key;
	private Object value;

	KV(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "[key="+key.toString() + ",value="+value.toString()+"]";
	}
}