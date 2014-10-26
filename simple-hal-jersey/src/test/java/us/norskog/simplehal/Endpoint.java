package us.norskog.simplehal;

import us.norskog.simplehal.Embedded;
import us.norskog.simplehal.Link;
import us.norskog.simplehal.LinkSet;
import us.norskog.simplehal.Links;

/*
 * Endpoint test for links
 */

public class Endpoint {

	@Links(linkset = @LinkSet(
			links = {
					@Link(rel = "self", href = "/", title = "Self") }
			)
			)
	public String getLinks() {
		return "[]";
	}

	@Links(linkset = @LinkSet(
			links = {
					@Link(rel = "self", href = "/", title = "Self") }
			),
			embedded = @Embedded(name = "thing2", items = "thing.thing2", links = 
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
