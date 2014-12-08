### SimpleHAL API
#### API
There are 6 features in the API: 4 annotations, one abstract class and the EL language.

**@_Links**

The `@_Links` annotation specifies a collection of top-level links.

**@Link** 

The `@Link` annotation specifies one link. It is only used inside `@_Links`.

**@_Embedded**

The `@_Embedded` annotations specifies set of links for items in a collections.

**@Items**

The `@Items` annotation specifies a set of links for an item in a collection.
It is only used inside @_Embedded annotations.

**Supplier**

This class provides two features:
* It can be used instead of an annotation to supply links to `@_Links` and `@_Embedded`.
* It is used as a template feature for annotations, and allows annotations to be re-used across multiple Jersey endpoints.

**EL Language**
The EL *expression language* is used inside link specification strings to substitute data items into links.

#### Usage
##### Jersey Endpoint
We'll demonstrate using SimpleHAL with this Jersey HTTP endpoint:
```
class MovieJson { String movieId, year, directors[]; }

@GET
@Path("movie")
public MovieJson getMovie(@PathParam("movieId") String movieId) {return new MovieJson...}
```
Let's call it via:

> curl **http://api.movielicio.us/movie/123456**

It will return this JSON:
```
{
  movieId:"123456",
  name:"Appalling Tripe"
  year:"1940",
  directors:[
    {directorId:"abc", name:"Reed Acted"},
    [directorId:"def", name:"Alan Smithee"}
  ]
}
```

##### **@_Links** example

```
@_Links(links = @Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie")))
```
Adds a top-level entry to the returned JSON:
```
_links:{[
  self:{href:"/movies/123456"},
  movieId:{href:"/movies/123456","title":"Appalling Tripe"}
]}
```
##### **@_Embedded** example
```
@_Embedded(links = {
		@Items(name = "directors", items = "${response.directors}", 
			links = { @Link(rel = "director", href = "/director/${item.value.directorId}", 
			title = "id ${item.value.name}"), more = {"index","${item.key}" })})
```
Adds another top-level entry to the returned JSON:
```
_embedded:{
    directors:[
    	{_links:{
          {href:"/director/abc", title:"Reed Acted", index:"1"},
          {href:"/director/def", title:"Alan Smithee", index:"2"}
      }}
    ]
  }
```
#### Advanced Usage
##### Supplier
The **Supplier** class adds two features to SimpleHAL:

* It can create links instead of using the annotations.
* It supplies a template feature so that an annotation need only be declared once.

This last is very important- the annotations are verbose and difficult to read, and block-copying them is bad practice. Java's Annotation feature does not supply templates by default. **Supplier** supplies a slightly hacky workaround for this lack by the simple expedient of allowing you to declare **@_Links** and **@_Embedded** on a method in the **Supplier** class rather than on Jersey endpoints. We will use the above annotations as an example:
```
public class MovieSupplier extends Supplier {
  @_Links(links = @Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie")))
  public Map<String,Object> getLinks(Object o) {;}
}
```
Suppose we have two more Jersey endpoints for creating & deleting movie records:
```
@PUT
@Path("movie")
public MovieJson putMovie(@PathParam("movieId") String movieId) {return new MovieJson...}

@DELETE
@Path("movie")
public MovieJson deleteMovie(@PathParam("movieId") String movieId) {return new MovieJson...}
```
We can remove the **@_Links** annotation from *movieGet()* and instead add this **@_Links** annotation to all three endpoints:
```
@_Links(linkset = MovieSupplier.class)
```
This uses the above Supplier class as a vessel for the original **@_Links** annotation.
