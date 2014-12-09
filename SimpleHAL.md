#SimpleHAL
SimpleHAL is a Jersey interceptor which adds optional **hyperlinks** to any json or xml returned by a Jersey service endpoint. SimpleHAL is unique because it allows specifying hyperlinks with Java annotations, rather than requiring new Java classes.

###Hyperlinks Explained

The core idea of hyperlinks is that the program knows how its API works, so it should export that knowledge instead of requiring the client to also know the details.  More concretely, each Json or XML message includes a set of links for various resources that are relevant to the answer. All of these links must fetch the underlying resource with an http GET.

For example, suppose a Movie link and returned json are:
```
  http://api.movielicio.us/movies/MOVIE_ID
```
```
 {
    "movieId":"123456",
    "year":"1940",
    "directors":[
      {"directorId":"abc", "name":"Reed Acted"},
      {"directorId":"def", "name":"Alan Smithee"}
  ]
}
```
With hyperlinks, all endpoints which address one Movie will also include this in the returned Json:
```
{
  _links:{
    "self":{"href":"/movies/123456"},
    "movieId":{"href":"/movies/123456","title":"Quickly Forgotten"},
    "year":{"href":"/years/1940"]
  }
  "_embedded":{
    "directors":[
	{"_links":{
          {"href":"/director/abc", "title":"Reed Acted"},
          {"href":"/director/def", "title":"Alan Smithee"}
        }]
  }
  ...
}
```
### Adding Hyperlinks with SimpleHAL
SimpleHAL adds hyperlinks to any Jersey endpoint with annotations only, no new code required. SimpleHAL works by adding annotations to Jersey HTTP endpoint methods. There are two kinds of hyperlinks available: _Links() supplies top-level links about resources related to the current resource. _Embedded() supplies sets of links about each member of an array or list.  

I lied about the no coding required part: creating hyperlinks requires programming, but not with Java. SimpleHAL uses the Java EL (Expression Language) feature to fill in data in a link. The data can be from the returned object or the parameters to the call. In the case of embedded links, one EL chooses an array or list of items from the returned data, and key or index value of the item and the item itself are available to the links for an item. 

With SimpleHAL, the Jersey HTTP endpoint for the above response looks like this:
```
@_Links(links = {
			@Link(rel = "movie", href = "/movie/${params.movieId}", title = "Movie"),
			@Link(rel = "year", href = "/year/${response.year}", title = "Year") })
@_Embedded(links = {
		@Items(name = "directors", items = "${response.directors}", 
			links = { @Link(rel = "director", href = "/director/${item.value.directorId}", 
			title = "id ${item.value.name}") })})
public MovieJson getMovie(@PathParam("movieId") String movieId) {...}

class MovieJson { String movieId, year, directors[]; }
```
In **@_Embedded**, the *items* expression chooses a collection from the response. *${item.value}* is the selected record. *${item.key}* is the index of the record in the collection. If *items* selects a substructure or map, *${item.key}* is the name of each item.

### Standards for Hyperlinks
There is no official standard for hyperlinks. This representation is based on a concrete spec that is an expired IETF draft standard.

> [https://tools.ietf.org/html/draft-kelly-json-hal-06#section-6](https://tools.ietf.org/html/draft-kelly-json-hal-06#section-6)

For an example of hyperlinks and why they are handy, see the RottenTomatoes API.
> [http://developer.rottentomatoes.com/docs](http://developer.rottentomatoes.com/doc)
