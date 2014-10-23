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

	private List<Map<String, List<Expression>>> links;
	private Map<String,List<Map<String, List<Expression>>>> embeddedLinks = null;

	Evaluator(ParsedLinkSet parsedLinkSet) {
		this.links = parse(parsedLinkSet.getLinks());
		Map<String, EmbeddedStore> embeddedMap = parsedLinkSet.getEmbeddedMap();
		if (embeddedMap != null) {
			this.embeddedLinks = new LinkedHashMap<String, List<Map<String,List<Expression>>>>();
			for(String name: embeddedMap.keySet()) {
				EmbeddedStore store = embeddedMap.get(name);
				embeddedLinks.put(store.getName(), parse(store.getLinks()));
			}
		}
	}

	public List<Map<String, List<Expression>>> parse(List<LinkStore> linkStore) {
		List<Map<String,List<Expression>>> parsed = new ArrayList<Map<String,List<Expression>>>();
		for(LinkStore store: linkStore) {
			Map<String, List<Expression>> link = new LinkedHashMap<String, List<Expression>>();
			for(String part: store.getParts().keySet()) {
				Parser p = new Parser(store.getParts().get(part));
				List<Expression> expressions = new ArrayList<Expression>();
				for(Expression e: p.getExpressions()) {
					expressions.add(e);
				}
				link.put(part, expressions);
			}
			parsed.add(link);
		}
		return parsed;
	}
	
	public Object evaluateExpr(String expr) {
		Object ob = executor.evalExpr(expr);
		return ob;
	}
	
	public List<Map<String, String>> evaluateLinks(Object response) {
		if (links == null)
			return null;
		return getLinks(response, null, links);
	}
	
	
	public List<Map<String, String>> evaluateEmbeddedItem(String name, Object response, Object item) {
		if (links == null || embeddedLinks == null)
			return null;
		return getLinks(response, item, embeddedLinks.get(name));
	}

//	
//	public Map<String, List<Map<String, String>>> evaluateEmbeddedItem(String name, Object response, Object item) {
//		if (links == null || embeddedLinks == null)
//			return null;
//		Map<String, List<Map<String, String>>> single = new HashMap<String, List<Map<String, String>>>();
//		List<Map<String, String>> links2 = getLinks(response, item, embeddedLinks.get(name));
//		single.put(name, links2);
//		return single;
//	}
	
	public List<Map<String, String>> getLinks(Object response, Object item, List<Map<String, List<Expression>>> linksType) {
		executor.setVar("response", response);
		if (item != null)
			executor.setVar("item", item);
		List<Map<String, String>> linkSet = new ArrayList<Map<String, String>>();
		for(Map<String, List<Expression>> link: linksType) {
			Map<String, String> linkSetPart = getLinkSet(link);
			if (linkSetPart != null)
				linkSet.add(linkSetPart);
		}
		return linkSet;
	}

	private Map<String, String> getLinkSet(Map<String, List<Expression>> link) {
		Map<String, String> linkSetPart = new LinkedHashMap<String, String>();
		for(String linkParts: link.keySet()) {
			StringBuilder sb = new StringBuilder();
			for(Expression expression: link.get(linkParts)) {
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

	public List<Map<String, List<Expression>>> getLinks() {
		return links;
	}

	public Map<String, List<Map<String, List<Expression>>>> getEmbeddedLinks() {
		return embeddedLinks;
	}

}
