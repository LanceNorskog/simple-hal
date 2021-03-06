### SimpleHAL Demo

This is a demo app for SimpleHAL. To try it out, run this in ../simple-hal-jersey:
```
mvn clean install
```

Come back over here and run:

```
mvn clean package exec:java
```

In another window, do a ReST request:

```
curl -s -H "Accept: application/json" http://localhost:8080/simplehal/links 
```

View the output in your favorite JSON viewer. (I recommend the 'json_pp' and 'jq' programs.)


```
{
  "doMap": true,
  "first": "one",
  "second": "two",
  "array": [
    "abc",
    "def"
  ],
  "list": [
    "ten",
    "eleven"
  ],
  "map": {
    "101": 101,
    "100": 100
  },
  "doFirst": true,
  "doArray": true,
  "doList": true
}
```
### Links
Change the above curl to accept mime-type `application/hal+json`:

```
curl -s -H "Accept: application/hal+json" http://localhost:8080/simplehal/links 
```

SimpleHAL adds this `_links` element to the returned JSON:

```
"_links": { 
  "first": { 
    "title": "First",
    "href": "/simplehal/links?id=one"
  },
  "self": { 
    "title": "Self",
    "href": "/simplehal/links"
  } 
}
```
The new `_links` element provides a handy set of URIs for data items given returned in the JSON block. `"self"` is the link you just called. `"first"` is a link to another resource provided by this webapp. (TODO: These links don't actually work.)

### Embedded lists
SimpleHAL can also return arrays of links, based on the data in the returned JSON block.
```
curl -q -H "Accept: application/hal+json" http://localhost:8080/simplehal/embedded 
```
SimpleHAL adds the above `_links` block and also this `_embedded` element to the returned JSON block:
```
 "_embedded": {
  "Mappacious": [
    {
      "_links": {
        "only": {
        "title": "#100",
        "href": "/simplehal/embedded?id=100"
        }
      }
    },
    {
      "_links": {
        "only": {
          "title": "#101",
          "href": "/simplehal/embedded?id=101"
        }
      }
    }
  ]
}
```
`_embedded` links are lists of items, while `_links` are top-level items. _One_-to-_many_ DB data can be returned with an entry in `_links` for the _one_ element, and an array in `_embedded` for the matching _many_ elements. 
#### Sources
The Jersey endpoint supplying these response is:

simplehal-webapp/src/java/us/norskog/simplehal/SimpleHalResource.java
#### Credits
`simplehal-webapp` is based on the Jersey hello-world webapp example:

`https://github.com/jersey/jersey/tree/master/examples/helloworld-webapp`

