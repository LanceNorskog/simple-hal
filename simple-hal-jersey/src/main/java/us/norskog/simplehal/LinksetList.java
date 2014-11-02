package us.norskog.simplehal;

import java.util.ArrayList;
import java.util.List;

class LinksetList extends ArrayList<LinksetMap> {

	public List lift() {
		List list = new ArrayList();
		for(LinksetMap lsmap: this) {
//			list.add(lsmap.lift());
		}
		return list;
	}
//	List<LinksetMap> linksetList;
//
//	public void setLinksetList(List<LinksetMap> linksList) {
//		this.linksetList = linksList;
//	}
//
//	public LinksetList() {
//	}
//	
//	
//	@Override
//	public String toString() {
//		return linksetList.toString();
//	}

}
