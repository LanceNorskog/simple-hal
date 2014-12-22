## HATEOS, HAL and Hyperlinks

SimpleHAL is an implementation of the [HATEOAS](http://en.wikipedia.org/wiki/HATEOAS) concept.
HATEOS provides useful features to the client of a ReST app:

* Discoverability
  * HATEOS allow the developer to learn how an API works by example. 
  The API documentation may not mention subtleties of the implementation, 
  details of the API may change without notice,
  or there may be no documentation at all.
* Knowledge Export
  * HATEOS exports knowledge of a ReST service's API to the client. This simplifies the client because it does not need to embed knowledge of how to use the API.
* Resource Style
  * HATEOS nudges the API towards a resource-based style. This means that the API provides unique, permanent links to resources available from the API. For example, a movie will always be represented by only one link and that link will always refer the same movie.
 
### Standards

The HAL Draft IRC is a concrete specification of the HATEOS concept. 

> [https://tools.ietf.org/html/draft-kelly-json-hal-06](https://tools.ietf.org/html/draft-kelly-json-hal-06)

There are several examples of HAL public APIs available, including a nice one by Rotten Tomatoes.

> [http://developer.rottentomatoes.com/docs](http://developer.rottentomatoes.com/doc)

Various implementations do not follow any common standard. The RottenTomatoes API has its own format, as did the Netflix API (_resquiat in pace_).
There are a few proposals and implementations for a common standard. 
The IETF HAL proposal is the only one I found that includes support for collections of items, not just top-level links. `_links`are top-level links and `_embedded` are collections.
Also, the [Spring HATEOS](http://spring.io/guides/gs/rest-hateoas) project uses the HAL format.

#### Note
The examples in the IETF draft allow the HAL blocks to contain information that is not in the outer blocks. 
SimpleHAL does not implement this aspect. 
A basic premise of SimpleHAL is that the HAL blocks are optional add-ons to the base JSON object, 
and so they cannot contain information which is not present in the original response. 

### Other Resources
[HalBuilder](https://github.com/HalBuilder) is a set of libraries for creating data in the HAL format for several Java-based languages.





