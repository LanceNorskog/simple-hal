simple-hal
==========

# Base library & annotation wrapper for Simple-HAL project.

Simple-HAL is an implementation of the [HATEOAS](http://en.wikipedia.org/wiki/HATEOAS) concept. The core idea of HATEOAS is very simple: a ReST service knows how it works, so why should a client program also need to know? Why can't the service tell the client how to use it?

Simple-HAL allows an HTTP ReST service to export basic uses of the service by adding information
to the response for a given request.
For example, a search page UI sends a keyword to a search engine, 
which returns a list of top 5 results for that keyword. With Simple-HAL, 
the engine can also export a link named 'next' to the UI. 
The UI then uses this link for a 'Next 5 results' button.
This make the UI code simpler because it does not embed code that creates the 'Next' link.

Simple-HAL adds two blocks of JSON to a returned json,
_links: a list of top-level links for the request and common navigation options for the request, and
_embedded: an (optional) list of links for a table of items returned by the request. It is easiest to explain this by example. Suppose a search page does a GET to this url:
```
http://zoo.phi.le/search?q=monkeys
```
The search engine will respond with this block of Json:

```
{
"q":"monkeys",
"start":0,
"rows":5,
[{"id":"547", "name":"What You Wanted", "url":"http://blah/foo/WhatYouWanted.pdf"},
 {"id":"462", "name":"Kind Of Relevant", "url":"http://blah/foo/KindOfRelevant.pdf"},
...
 {"id":"7888", "name":"Snow Monkeys in a Hot Tub", "url":"http://blah/foo/SnowMoneys.png"}
]
}
```
This response tells the UI what it needs to know to make a Next button, but the UI developer has to add code for the Next button. Simple-HAL can add an extra block of Json to the result:
```
"_links": {
[{ "name":"self", "href": "http://zoo.phi.le/search?q=monkeys" },
 { "name":"next", "title":"Next", "href": "http://zoo.phi.le/search?q=monkeys&start=5&rows=5" },
]}
```
Now, the UI can just hunt for the element with _name_="next' and put _title_ and _href_ in the UI.

Simple-HAL can also add ready-made display information for all of the search results.
```
"_embedded": {
     "Constance" : [
         [
            {
               "rel" : "only",
               "title" : "id 0",
               "href" : "http://localhost:9998/helloworld/embedded?id=hello"
            }
         ]
      ],

```
Simple-HAL specifies these links with text specifiers only. There is no ability to supply code to implement the additions.

Specification format:
rel:/path or rel:title:/path
where title does not start with a /. It could use the %xxx code for /.
What is in front of /path?

@Links(
	@LinkSet(
    @Link(rel="self",href="/orders",title="Orders", {more attribute pairs as simple string array}),...)
@Embedded(item="${a.b}", links={
    @Link(rel="self",href="/path/order/${order}", ... }
    )
    @Embedded(
        path = "a.b means a.getB()",
        @LinkSet(... as above but with "item." available)
        )
    )
)

Sample specs for Solr search engine:
{rel = "first",href= "/search/q=${request.q}&start=0"&${response.rows}
{rel="prev",href= "/search/q=${request.q}&start="${response.start- response.rows"}
{rel="next",href= "/search/q=${request.q}&start="${response.start+ response.rows"}
{rel="last", href="/search/q=${request.q}&start="10000000000"&rows=${response.rows}}

Note: start and rows can be the defaults and not included in the params,
so must be included in the return object so that the evals can find them.

embedded: use ${response.results} {
[{title="${... item.title ...}", href="${... item.url ...}"]
}

EL spec:
three variables: request, response and item. item is the row item found by the embedded search path.
item can be an object, Map or an array of items. it cannot be a base type.
ELs are run under a SecurityManager object.

Link annotation includes 'check' expression to decide whether to emit the link. Defaults to true.

-----
Simple-HAL is a basic implementation of the HAL hyperlink standard for Java by M. Kelly. 
There is no reason to force this, but it's the only spec that include "_embedded" which make it actually useful. 
The current spec:
https://tools.ietf.org/html/draft-kelly-json-hal-06
The examples in this draft allow the HAL blocks to contain information that is not in the outer blocks. 
A basic premise of Simple-HAL is that the HAL blocks are add-ons to the base Json object. 
They only contain generated links, no original data.
