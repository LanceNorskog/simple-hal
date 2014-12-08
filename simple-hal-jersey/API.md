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

##### **@_Links**

```
@_Links(links = @Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie")))
```
This will add a top-level entry to the returned JSON:
```
_links:{[
  self:{href:"/movies/123456"},
  movieId:{href:"/movies/123456","title":"Appalling Tripe"}
]}
```

