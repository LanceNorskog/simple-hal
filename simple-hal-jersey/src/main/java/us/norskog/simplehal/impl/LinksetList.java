package us.norskog.simplehal.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * _links:
 * _embedded: "orders":["x":{"_links":[...
 * @author lance
 *
 */

@SuppressWarnings("serial")
class LinksetList extends ArrayList<LinksetMap> {

	public LinksetList() {;}
//	public LinksetList(List<Map> links) {
//		for(Map map: links) {
//			this.add(new LinksetMap((Map) map));
//		};
//	}
}
