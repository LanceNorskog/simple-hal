package us.norskog.simplehal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Build return links and embedded trees from parsed annotations, return object.
 *
 */

public class Builder {

	public LinksetMap buildLinks(ParsedLinkSet parsedLinkSet,
			Evaluator evaluator, Map<String, Object> response) {
		Map<String, Map<String, String>> expanded = evaluator.evaluateLinks(response);
//		mapify(expanded);
		LinksetMap lsm = new LinksetMap();
		lsm.setMap(expanded);
		return lsm;
	}

	public Map<String, EmbeddedMap> buildEmbedded(ParsedLinkSet parsedLinkSet,
			Evaluator evaluator, Map<String, Object> response) {
		if (parsedLinkSet.getEmbeddedMap() != null) {
			List<ItemStore> storeList = parsedLinkSet.getEmbeddedMap();
			Map<String, EmbeddedMap> _embedded = new HashMap<String, EmbeddedMap>();
			for(ItemStore store: storeList) {
				LinksetList embeddedLinks = new LinksetList();
//				embeddedLinks.setLinksetList(new ArrayList<LinksetMap>());
				EmbeddedMap _links = new EmbeddedMap();
				_links.put("_links", embeddedLinks);
				_embedded.put(store.getName(), _links);
				if (store.getPath() != null) {
					LinksetMap links = new LinksetMap();
					Object items = evaluator.evaluateExpr(store.getPath());
					if (items == null)
						continue;
					if (items.getClass().isArray()) {
						Object[] obs = (Object[]) items;
						for(int i = 0; i < obs.length; i++) {
							KV kv = new KV(Integer.toString(i), obs[i]);
							links.setMap(evaluator.evaluateEmbeddedItem(store.getName(), response, kv));
							if (links.size() > 0)
								embeddedLinks.add(links);	
//							mapify(links);
						}
					} else if (items instanceof Map) {
						for(Object key: ((Map<String,Object>) items).keySet()) {
							KV kv = new KV(key.toString(), ((Map<?, ?>) items).get(key.toString()));
							links.setMap(evaluator.evaluateEmbeddedItem(store.getName(), response, kv));
							if (links.size() > 0)
								embeddedLinks.add(links);	
//							mapify(links);

						}
					} else if (items instanceof Collection) {
						int i = 0;
						for(Object ob: (Collection<?>) items) {
							KV kv = new KV(Integer.toString(i), ob);
							links.setMap(evaluator.evaluateEmbeddedItem(store.getName(), response, kv));
							if (links.size() > 0)
								embeddedLinks.add(links);	
//							mapify(links);

							i++;
						}
					} else {
						KV kv = new KV("0", items);
						links.setMap(evaluator.evaluateEmbeddedItem(store.getName(), response, kv));
						if (links.size() > 0)
							embeddedLinks.add(links);	
//						mapify(links);

					}
				}
			}
//			mapify(_embedded);
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
			System.out.println(objectAsMap.toString());
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

}
