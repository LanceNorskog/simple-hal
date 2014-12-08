### Advanced Usage
##### *Check* Expression

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



