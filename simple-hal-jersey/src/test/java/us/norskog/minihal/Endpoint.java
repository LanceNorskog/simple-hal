package us.norskog.minihal;

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
			embedded = @Embedded(name = "thing2", path = "thing.thing2", links = 
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
