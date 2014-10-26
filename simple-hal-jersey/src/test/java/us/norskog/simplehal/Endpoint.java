package us.norskog.simplehal;

import us.norskog.simplehal.ItemSet;
import us.norskog.simplehal.Link;
import us.norskog.simplehal.LinkSet;
import us.norskog.simplehal._Links;

/*
 * Endpoint test for links
 */

public class Endpoint {

	@_Links(linkset = @LinkSet(
			links = {
					@Link(rel = "self", href = "/", title = "Self") }
			)
			)
	public String getLinks() {
		return "[]";
	}

	@_Links(linkset = @LinkSet(
			links = {
					@Link(rel = "self", href = "/", title = "Self") }
			),
			embedded = @ItemSet(name = "thing2", items = "thing.thing2", links = 
			@LinkSet(
					links = {
							@Link(rel = "self", href = "/", title = "Self") }
					)
					)
			)
	public String getAll() {
		return "[]";
	}

}
