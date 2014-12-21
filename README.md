SimpleHAL
==========

SimpleHAL allows an HTTP ReST service to export basic uses of the service by adding information
to the response for a given request.
SimpleHAL adds two elements to returned JSON block:

`_links`: a list of top-level links for the request and common navigation options for the request, and

`_embedded`: an (optional) list of links for a collection of items returned by the request. 

SimpleHAL is a toolkit for the Java Jersey API. This project has two components: 
* simple-hal-jersey is the code for a Jersey "interceptor".
* simple-hal-webapp is a demo Jersey app.
See the READMEs in both directories for more.

===== Details
SimpleHAL is an implementation of the [HATEOAS](http://en.wikipedia.org/wiki/HATEOAS) concept. 

For more on HATEOS and the purpose of this toolkit, see Hyperlinks.md in this directory. For more on how to use SimpleHAL and why it will make your life wonderful, see SimpleHAL.md in this directory.

===== Specification
Simple-HAL is a basic implementation of the HAL hyperlink standard for Java by M. Kelly. 
There is no reason to force this, but it's the only spec that include "_embedded" which make it actually useful. 
The current spec:
```
https://tools.ietf.org/html/draft-kelly-json-hal-06
```
The examples in this draft allow the HAL blocks to contain information that is not in the outer blocks. SimpleHAL does not implement this aspect. A basic premise of SimpleHAL is that the HAL blocks are add-ons to the base JSON object, and so they cannot contain information which is present in the original response. 

get netflix & rottentomatoes
=== Other Resources
This is a set of libraries for creating data in the HAL format for several Java-based languages:
```
https://github.com/HalBuilder
```

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

