## HATEOS, HAL and Hyperlinks

Simple-HAL is an implementation of the [HATEOAS](http://en.wikipedia.org/wiki/HATEOAS) concept. 
The core idea of HATEOAS is very simple: a ReST service knows how it works, 
so why should a client program also need to know? Why can't the service tell the client how to use it?
The HAL Draft IRC is a concrete specification of the HATEOS concept. 

> [https://tools.ietf.org/html/draft-kelly-json-hal-06](https://tools.ietf.org/html/draft-kelly-json-hal-06)

There are several examples of HAL public APIs available, including a nice one by Rotten Tomatoes.

> [http://developer.rottentomatoes.com/docs](http://developer.rottentomatoes.com/doc)

Various implementations do not follow any common standard. The RottenTomatoes API has its own format, as did the Netflix API (_resquiat in pace_).
There are a few proposals and implementations for a common standard. 
The IETF HAL proposal is the only one I found that includes support for collections of items, not just top-level links. `_links` are top-level links and `_embedded` are collections.
Also, the Spring Hyperlinks project uses the HAL format.





