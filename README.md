SimpleHAL
==========

SimpleHAL allows a Jersey-based HTTP ReST service to export basic uses of the service by adding information
to the ReST responses.
SimpleHAL adds two elements to returned JSON block:

`_links`: a list of top-level links for the request and common navigation options for the request, and

`_embedded`: an (optional) list of links for a collection of items returned by the request. 

SimpleHAL is a toolkit for the Java Jersey API. This project has two components: 
* simple-hal-jersey is the code for a Jersey "interceptor".
* simple-hal-webapp is a demo Jersey app.
See the READMEs in both directories for more.

## Details
SimpleHAL is an implementation of the [HATEOAS](http://en.wikipedia.org/wiki/HATEOAS) concept. 

For more on how to use SimpleHAL and why it will make your life wonderful, see SimpleHAL.md in this directory.
For more on HATEOS and the context of this toolkit, see Hyperlinks.md in this directory. 

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

