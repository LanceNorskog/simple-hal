package us.norskog.simplehal.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import us.norskog.simplehal.Supplier;
import us.norskog.simplehal.Items;
import us.norskog.simplehal.Link;
import us.norskog.simplehal._Embedded;
import us.norskog.simplehal._Links;

/*
 * Unpack Links/Link/Embedded annotation structure
 * into named objects.
 */

public class ParsedLinkSet {
	public final static String REL = "rel";
	public final static String TITLE = "title";
	public final static String HREF = "href";
	private final static Map<Integer, ParsedLinkSet> parsedAnnos = new ConcurrentHashMap<Integer, ParsedLinkSet>();

	private Annotation[] annos = null;
	private List<LinkStore> links = null;
	private List<ItemStore> embeddedItems = null;

	public ParsedLinkSet(Annotation[] annos) {
		this.annos = annos;
		_Links _linksAnno = (_Links) getAnno(annos, _Links.class);
		_Embedded _embeddedAnno = (_Embedded) getAnno(annos, _Embedded.class);
		if (Supplier.class.isAssignableFrom(_linksAnno.linkset())) {
			_linksAnno = getHyperAnnos(_Links.class, _linksAnno.linkset());
		}
		if (_linksAnno != null) {
			Link[] linkSpecs = _linksAnno.links();
			links = storeLinks(linkSpecs);
		}
		if (_embeddedAnno != null) {
			if (Supplier.class.isAssignableFrom(_embeddedAnno.linkset())) {
				_embeddedAnno = getHyperAnnos(_Embedded.class, _embeddedAnno.linkset());
			}
			Items[] items = _embeddedAnno.links();
			if (items.length > 0) {
				embeddedItems = new ArrayList<ItemStore>();
				for(int i = 0; i < items.length; i++) {
					Items item = items[i];
					ItemStore store = storeItem(item);
					embeddedItems.add(store);
				}
			}
		}
	}

	// TODO: change to links/embedded
	private <T> T getHyperAnnos(Class<T> _annoClass, Class linkset) {
		if (! Supplier.class.isAssignableFrom(linkset)) {
			return null;
		}
		try {
			Method m = linkset.getMethod("getLink", Object.class);
			return (T) m.getAnnotation((Class) _annoClass);
		} catch (NoSuchMethodException e) {
			// Cannot happen
			e.printStackTrace();
		} catch (SecurityException e) {
			// Cannot happen
			e.printStackTrace();
		}
		
		
		return null;
	}

	private ItemStore storeItem(Items items) {
		List<LinkStore> itemLinks = storeLinks(items.links());
		ItemStore store = new ItemStore(items.name(), items.items(), itemLinks);
		return store;
	}

	private List<LinkStore> storeLinks(Link[] linkset) {
		List<LinkStore> links = new ArrayList<LinkStore>(); 
		for(Link link: linkset) {
			LinkStore store = new LinkStore(link.rel(), link.title(), link.href());
			if (! link.check().equals("${true}"))
				store.setCheck(link.check());
			Object[] moreOb = link.more();
			if (moreOb.length > 0) {
				try {
					if (moreOb instanceof String[]) {
						String[] more = (String[]) moreOb;
						for(int i = 0; i < more.length; i+=2) {
							String key = more[i];
							String value = more[i + 1];
							store.addPart(key, value);
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					store.addPart("error", "illegal more clause: odd number of values");
				}
			}
			links.add(store);
		}
		return links;
	}

	public List<LinkStore> getLinks() {
		return links;
	}

	public List<ItemStore> getEmbeddedMap() {
		return embeddedItems;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(annos);
		result = prime * result
				+ ((embeddedItems == null) ? 0 : embeddedItems.hashCode());
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParsedLinkSet other = (ParsedLinkSet) obj;
		if (!Arrays.equals(annos, other.annos))
			return false;
		if (embeddedItems == null) {
			if (other.embeddedItems != null)
				return false;
		} else if (!embeddedItems.equals(other.embeddedItems))
			return false;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		return true;
	}

	public static ParsedLinkSet getParsedLinkSet(Annotation[] annos) {
		_Links links = (_Links) getAnno(annos, _Links.class);
		_Embedded embedded = (_Embedded) getAnno(annos, _Embedded.class);
		Integer code = new Integer(links.hashCode() ^ (embedded == null ? 0 : embedded.hashCode()));
		ParsedLinkSet set = parsedAnnos.get(code);
		if (set != null) {
			return set;
		} else {
			set = new ParsedLinkSet(annos);
			parsedAnnos.put(code, set);
			return set;
		}
	}

	private static Annotation getAnno(Annotation[] annos, Class<? extends Annotation> annoClass) {
		for(Annotation anno: annos) {
			if (anno.annotationType().equals(annoClass)) {
				return anno;
			}
		}
		return null;
	}

}

class LinkStore {
	private String name;
	private String check = "true";
	private Map<String,String> parts = new HashMap<String, String>();

	public LinkStore(String rel, String title, String[] href) {
		if (rel == null)
			rel = "#NULL";
		parts.put(ParsedLinkSet.REL, rel);
		if (title != null)
			parts.put(ParsedLinkSet.TITLE, title);
		if (href == null)
			parts.put(ParsedLinkSet.HREF, "#NULL");
		else {
			StringBuilder sb = new StringBuilder();
			for(String bit: href)
				sb.append(bit);
			parts.put(ParsedLinkSet.HREF, sb.toString());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String,String> getParts() {
		return parts;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public void addPart(String key, String value) {
		parts.put(key, value);
	}

}

class ItemStore {
	private final String name;
	private final String path;
	private final List<LinkStore> links;
	private String check = "";

	public ItemStore(String name, String path, List<LinkStore> links) {
		this.name = name;
		this.path = path;
		this.links = links;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public List<LinkStore> getLinks() {
		return links;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}


}