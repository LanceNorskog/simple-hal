package us.norskog.simplehal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Store and evaluate link sets
 * 
 * @author lance
 *
 */

public class Evaluator {
	Executor executor = new Executor();

	private evLinkSet evLinkSet = null;
	private evEmbeddedSet evEmbeddedSet = null;

	Evaluator(ParsedLinkSet parsedLinkSet) {
		this.evLinkSet = parse(parsedLinkSet.getLinks());
		Map<String, EmbeddedStore> embeddedMap = parsedLinkSet.getEmbeddedMap();
		if (embeddedMap != null) {
			evEmbeddedSet = new evEmbeddedSet();
			for(String name: embeddedMap.keySet()) {
				EmbeddedStore store = embeddedMap.get(name);
				evLinkSet parsedEmbedded = parse(store.getLinks());
				evEmbedded embedded = new evEmbedded();
				embedded.name = name;
				embedded.evLinkSet = parsedEmbedded;
				evEmbeddedSet.evEmbedded.put(name, embedded);
			}
		}
	}

	private evLinkSet parse(List<LinkStore> linkStore) {
		evLinkSet parsed = new evLinkSet();
		for(LinkStore store: linkStore) {
			evLink link = new evLink();
			for(String part: store.getParts().keySet()) {
				Parser p = new Parser(store.getParts().get(part));
				List<Expression> expressions = new ArrayList<Expression>();
				for(Expression e: p.getExpressions()) {
					expressions.add(e);
				}
				link.parts.put(part, expressions);
			}
			parsed.evLinks.add(link);
		}
		return parsed;
	}
	
	public Object evaluateExpr(String expr) {
		Object ob = executor.evalExpr(expr);
		return ob;
	}
	
	public List<Map<String, String>> evaluateLinks(Object response) {
		if (evLinkSet == null)
			return null;
		List<Map<String, String>> linkSet = getLinks(response, null, evLinkSet);
		return linkSet;
	}
	
	
	public List<Map<String, String>> evaluateEmbeddedItem(String name, Object response, Object item) {
		if (evLinkSet == null || evEmbeddedSet == null)
			return null;
		return getLinks(response, item, evEmbeddedSet.evEmbedded.get(name).evLinkSet);
	}

	private List<Map<String, String>> getLinks(Object response, Object item, 
			evLinkSet evLinkSet) {
		executor.setVar("response", response);
		if (item != null)
			executor.setVar("item", item);
		List<Map<String, String>> linkSet = new ArrayList<Map<String, String>>();
		for(evLink evLink: evLinkSet.evLinks) {
			Map<String, String> linkSetPart = getLink(evLink);
			if (linkSetPart != null)
				linkSet.add(linkSetPart);
		}
		return linkSet;
	}

	private Map<String, String> getLink(evLink evLink) {
		Map<String, String> linkSetPart = new LinkedHashMap<String, String>();
		for(String linkParts: evLink.parts.keySet()) {
			StringBuilder sb = new StringBuilder();
			for(Expression expression: evLink.parts.get(linkParts)) {
				Object evaluated = expression.eval(executor);
				if (evaluated == null)
					return null;
				String value = evaluated.toString();	
				sb.append(value);
			}
			linkSetPart.put(linkParts, sb.toString());
		}
		return linkSetPart;
	}

}

class evLinkSet {
	List<evLink> evLinks = new ArrayList<evLink>();
}

class evLink {
	Map<String, List<Expression>> parts = new HashMap<String, List<Expression>>();
	Expression check;
}

class evEmbeddedSet {
	Map<String, evEmbedded> evEmbedded = new HashMap<String, evEmbedded>();
}

class evEmbedded {
	public evLinkSet evLinkSet;
	String name;
	List<evLinkSet> evLinks = new ArrayList<evLinkSet>();
}
