simple-hal
==========

# Base library & annotation wrapper for Simple-HAL project.

Simple-HAL is a basic implementation of the HAL hyperlink standard for Java by M. Kelly. 
There is no reason to force this, but it's the only spec that include "_embedded" which make it actually useful. 
The current spec:
https://tools.ietf.org/html/draft-kelly-json-hal-06
The examples in this draft allow the HAL blocks to contain information that is not in the outer blocks. 
A basic premise of Simple-HAL is that the HAL blocks are add-ons to the base Json object. 
They only contain generated links, no original data.


A ReST application knows what its HTTP API does, so why should the application also know?
HAL allows the application to export basic (obvious) uses of the API with a return value for a given request.
For example, a search engine returns a list of N results for a keyword search. With HAL, the engine can also
export a link named 'next' to the UI. The UI then uses this link for a 'Next N results' button.
This allows the UI to not embed code for how to create the 'Next' link.

Simple-HAL adds two blocks of JSON to a returned json,
_links: a list of top-level links for the request and common navigation options for the request, and
_embedded: an (optional) list of links for a table of items returned by the request.
PLACEHOLDER UNTIL SOFTWARE PROVIDES COHERENT EXAMPLE:

     "_links": {
       "self": { "href": "/orders" },
       "next": { "href": "/orders?page=2" },
     }

     "_embedded": {
       "orders": [{
           "_links": {
             "self": { "href": "/orders/123" },
             "basket": { "href": "/baskets/98712" },
             "customer": { "href": "/customers/7809" }
           },
          },{
           "_links": {
             "self": { "href": "/orders/124" },
             "basket": { "href": "/baskets/97213" },
             "customer": { "href": "/customers/12369" }
           },
        }]
     },
     "orders":[{
         "order": "123",
         "basket": "98712",
         "customer": "7809",
         "total": 30.00,
         "currency": "USD",
         "status": "shipped",
     },{
         "order": "124",
         "basket": "98713",
         "customer": "12369",
         "total": 20.00,
         "currency": "USD",
         "status": "processing"
     }],
     "currentlyProcessing": 14,
     "shippedToday": 20
   }

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
