package us.norskog.simplehal.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Store and evaluate link sets
 * 
 * @author lance
 *
 */

public class Evaluator {
	private static String TRUE = "true"; // default for all check expressions
	Executor executor = new Executor();

	private evLinkSet evLinkSet = null;
	private evEmbeddedSet evEmbeddedSet = null;

	Evaluator(ParsedLinkSet parsedLinkSet) {
		this.evLinkSet = parse(parsedLinkSet.getLinks());
		List<ItemStore> embeddedMap = parsedLinkSet.getEmbeddedMap();
		if (embeddedMap != null) {
			evEmbeddedSet = new evEmbeddedSet();
			for(ItemStore store: embeddedMap) {
				evLinkSet parsedEmbedded = parse(store.getLinks());
				evEmbedded embedded = new evEmbedded();
				embedded.name = store.getName();
				embedded.evLinkSet = parsedEmbedded;
				evEmbeddedSet.evEmbedded.put(store.getName(), embedded);
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
			if (! store.getCheck().equals(TRUE)) {
				Parser p = new Parser(store.getCheck());
				link.check = p.expressions;
			}
			parsed.evLinks.add(link);
		}
		return parsed;
	}
	
	public Object evaluateExpr(String expr) {
		Object ob = executor.evalExpr(expr);
		return ob;
	}
	
	public Map<String, Map<String, String>> evaluateLinks(Object response) {
		if (evLinkSet == null)
			return null;
		Map<String, Map<String, String>> linkSet = getLinks(response, null, evLinkSet);
		return linkSet;
	}	
	
	public Map<String, Map<String, String>> evaluateEmbeddedItem(String name, Object response, Object item) {
		if (evLinkSet == null || evEmbeddedSet == null)
			return null;
		return getLinks(response, item, evEmbeddedSet.evEmbedded.get(name).evLinkSet);
	}

	private Map<String,Map<String, String>> getLinks(Object response, Object item, 
			evLinkSet evLinkSet) {
		executor.setVar("response", response);
		if (item != null)
			executor.setVar("item", item);
		Map<String,Map<String, String>> linkSet = new HashMap<String, Map<String,String>>();
		for(evLink evLink: evLinkSet.evLinks) {
			Map<String, String> linkSetPart = getLink(evLink);
			if (linkSetPart != null) {
				String name = linkSetPart.get("rel");
				linkSetPart.remove("rel");
				linkSet.put(name,linkSetPart);
			}
		}
		return linkSet;
	}

	private Map<String, String> getLink(evLink evLink) {
		if (evLink.check != null) {
			String evaled = evaluateExpression(evLink.check);
			if (!evaled.equals("1") && !evaled.equals("true")) 
				return null;
		}
		Map<String, String> linkSetPart = new HashMap<String, String>();
		for(String linkParts: evLink.parts.keySet()) {
			String evaled = evaluateExpression(evLink.parts.get(linkParts));
			if (evaled == null)
				evaled = "#NULL";
			linkSetPart.put(linkParts, evaled);
		}
		return linkSetPart;
	}

	private String evaluateExpression(List<Expression> parts) {
		StringBuilder sb = new StringBuilder();
		for(Expression expression: parts) {
			Object evaluated = expression.eval(executor);
			if (evaluated == null)
				return null;
			String value = evaluated.toString();	
			sb.append(value);
		}
		return sb.toString();
	}

}

class evLinkSet {
	List<evLink> evLinks = new ArrayList<evLink>();
}

class evLink {
	Map<String, List<Expression>> parts = new HashMap<String, List<Expression>>();
	List<Expression> check;
}

class evEmbeddedSet {
	Map<String, evEmbedded> evEmbedded = new HashMap<String, evEmbedded>();
}

class evEmbedded {
	public evLinkSet evLinkSet;
	String name;
	List<evLinkSet> evLinks = new ArrayList<evLinkSet>();
}
