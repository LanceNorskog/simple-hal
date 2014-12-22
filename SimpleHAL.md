
#SimpleHAL
SimpleHAL is a Jersey interceptor which adds optional **hyperlinks** to any JSON returned by a Jersey service endpoint. SimpleHAL is unique because it allows specifying hyperlinks with Java annotations, rather than requiring new Java classes.

###Hyperlinks Explained

The core idea of hyperlinks is that the program knows how its API works, so it should export that knowledge instead of requiring the client to also know the details.  More concretely, each JSON or message includes a set of links for various resources that are relevant to the answer. All of these links must fetch the underlying resource with an http GET.

For example, suppose a Movie link and returned JSON are:
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
With hyperlinks, all endpoints which address one Movie will also include this in the returned JSON:
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
SimpleHAL adds hyperlinks to any Jersey endpoint with Java annotations. 
There are two annotation classes: 
* `@_Links()` supplies top-level links about resources related to the current resource. 
* `@_Embedded()` supplies sets of links about each member of an array or list.  

Creating hyperlinks requires programming, but not with Java. SimpleHAL uses the Java EL (Expression Language) to fill in data in a link. The data can be from the returned object or the parameters to the call. In the case of embedded links, one EL chooses an array or list of items from the returned data, and key or index value of the item and the item itself are available to the links for an item. 

With SimpleHAL, the Jersey HTTP endpoint for the above response looks like this:
```
@_Links(links = {
			@Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie"),
			@Link(rel = "year", href = "/year/${response.year}", title = "Year") })
@_Embedded(links = {
		@Items(name = "directors", items = "${response.directors}", 
			links = { @Link(rel = "director", href = "/director/${item.value.directorId}", 
			title = "id ${item.value.name}") })})
public MovieJson getMovie(@PathParam("movieId") String movieId) {...}

class MovieJson { String movieId, year; List<Director> directors; ** getters for these fields **}
class Director {String directorId, name; ** getters for these fields ** }
```

#### Variables in EL expressions
There are two variables available in EL expressions, `response` and `item`.
* `response` is the JSON block returned by the endpoint. It can be a Java POJO with getter methods or a Map.
* `item` is the element in a collection that is specified by the `items` EL expression.

In both **@_Links** and **@_Embedded** the `response` object has getters for movieId, year and list of directors.
`$(response.movieId}` calls the getter method and fills in the returned string into the link.
In **@_Embedded**, the *items* expression chooses a collection from the response. `${item.value}` is the selected record.
 `{item.key}` is the index of the record in the collection. 
If *items* selects a substructure or map, *${item.key}* is the name of each item.

EL expressions only call Java POJO getters or Map lookups. 
They do not look directly at fields or call any other code in an object.
They are not vulnerable to injection attacks. (This may require using Java SecurityManager objects.)


