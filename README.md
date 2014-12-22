SimpleHAL
==========

SimpleHAL lets you add hyperlinks to a Jersey app with Java annotations, no coding required.

SimpleHAL adds two elements to returned JSON blocks:

`_links`: a list of top-level links for the request and common navigation options for the request, and

`_embedded`: an (optional) list of links for a collection of items returned by the request. 

SimpleHAL is a toolkit for the Java Jersey API. This project has two directories: 
* simple-hal-jersey is the code for a Jersey "interceptor".
* simple-hal-webapp is a demo Jersey app.
See the READMEs in both directories for more.

## Try it out!
`simplehal-webapp/` is a simple Jersey demo of SimpleHAL features: 

```
cd simplehal-jersey
mvn clean install
cd ../simplehal-webapp
mvn clean package exec:java
```

See simplehal-webapp/README.md for more.

## Why?
SimpleHAL is an implementation of the [HATEOAS](http://en.wikipedia.org/wiki/HATEOAS) concept. 
HATEOS is a clear win for the API world because it simplifies clients, 
and there are (usually) many more clients than services.
Adoption of HATEOS has lagged for a few reasons:
* Lack of a standard
* Lack of demand
* Effort of implementation

SimpleHAL remedies this last problem by making it as easy as possible to add hyperlinks to a Jersey-based ReST service. 
You don't have to write any Java code to add hyperlinks: 
SimpleHAL specifies hyperlinks via strings in Java annotations.
If you have a Jersey endpoint to returns information about a movie, 
this annotation will add links for the movie itself and and the director:

```
@_Links(links = {
			@Link(rel = "movie", href = "/movie/${response.movieId}", title = "Movie"),
			@Link(rel = "director", href = "/directors/${response.directorId}", title = "Director") })
```

A client for this API only needs to know how to fetch a movie.
It does not need to know how to fetch the director, because the API has exported this knowledge.
If the client is a UI, it can even use the `title` field to populate the link fields.

There are other toolkits for this purpose, but they require writing Java code.
SimpleHAL only requires Java annotations, strings and code in the Java EL expression language.

## More Info

For more on how to use SimpleHAL and why it will make your life wonderful, see SimpleHAL.md in this directory.
For more on HATEOS and the context of this toolkit, see Hyperlinks.md in this directory. 

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

