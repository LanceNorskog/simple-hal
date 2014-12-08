### Advanced Usage
##### Check Expression

Every link specification may include a *check* expression which decides whether or not to include the link. 
The *check* expression should be a boolean EL expression with nothing outside the braces.

This example shows how to use SimpleHAL to paginate through search engine results. This is the JSON returned by a search query:
```
{
  q:"monkeys",
  offset:0,
  rows:10,
  total:34
}
```
*offset* is the position of the first result in the set. *rows" is the number of results to return. *total* is the total number of search results available. This set of links can incrementally page through the result set by supplying :
```
@_Links(links = {
  @Link(rel = "current", href = {"/movie?q=${response.q}&offset=${response.offset}&rows=${response.rows}", title = "Current"},
  @Link(rel = "first", href = {"/movie?q=${response.q}&offset=0&rows=${response.rows}", title = "First"},
  @Link(rel = "next", href = {"/movie?q=${response.q}&offset=${response.offset + response.rows}&rows=${response.rows}"
   title = "Next", check = "${response.total - response.offset > $response.rows}"},
  @Link(rel = "prev", href = {"/movie?q=${response.q}&offset=${response.offset - response.rows}&rows=${response.rows}"
    title = "Previous", check = "${response.offset > 0}"},
  @Link(rel = "last", href = {"/movie?q=${response.q}&offset=${response.total - response.rows}&rows=${response.rows}", title = "Last"}
})
```
Each link can be used verbatim for the First, Next and Previous buttons in a search UI. Next and Previous use check expressions to decide whether or not that link makes sense. If we're at the end of the list, there's no Next link. If we're at the beginning of the list, there's no Previous link. 

##### **Supplier** example
The **Supplier** interface adds two features to SimpleHAL:

* It can create links instead of using the annotations.
* It supplies a template feature so that an annotation need only be declared once.

Java's Annotation feature does not supply templates by default. **Supplier** gives a slightly hacky workaround for this lack by the simple expedient of allowing you to declare **@_Links** and **@_Embedded** on a method in the **Supplier** class rather than on Jersey endpoints. We will use the above annotations as an example:
```
public class MovieSupplier extends Supplier {
  @_Links(links = @Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie")))
  public Map<String,Object> getLinks(Object o) {;}
}
```
Suppose we have three Jersey endpoints for creating & deleting movie records:
```
class MovieJson { String movieId, year, directors[]; }

@GET
@Path("movie")
@_Links(links = @Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie")))
public MovieJson getMovie(@PathParam("movieId") String movieId) {return new MovieJson...}

@PUT
@Path("movie")
@_Links(links = @Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie")))
public MovieJson putMovie(@PathParam("movieId") String movieId) {return new MovieJson...}

@DELETE
@Path("movie")
@_Links(links = @Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie")))
public MovieJson deleteMovie(@PathParam("movieId") String movieId) {return new MovieJson...}
```
We can remove the **@_Links** annotation from *movieGet()* and instead add this **@_Links** annotation to all three endpoints:
```
@_Links(linkset = MovieSupplier.class)
```
This uses the above Supplier class as a vessel for the original **@_Links** annotation. Real-life use of **@_Links** will be a few lines of text and use of **@_Embedded** can be several lines of text.

There can be several Supplier classes instead of just one. For example, if MovieJson includes a 'year' field and there is a search endpoint */year/{number}* one might also create a YearSupplier class for the year search **_@Links** annotation. The **@_Links** annotation for all three endpoints would then be:
```
@_Links(linkset = {MovieSupplier.class, YearSupplier.class})
```


