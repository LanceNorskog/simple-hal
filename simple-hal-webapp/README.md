This is a demo app for SimpleHAL. To try it out:

* 'mvn clean install' in simplehal-jersey

* 'mvn clean package exec:java' in simplehal-webapp

* in another window, run:
```
curl -s -H "Accept: application/json" http://localhost:8080/simplehal/links 
```
And view the output in your favorite Json viewer. (I recommend the 'json_pp' or 'jq' programs.)

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
=== Links
Change the above curl to accept mime-type `application/hal+json`:
```
curl -s -H "Accept: application/json" http://localhost:8080/simplehal/links 
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
`_links` provides a handy set of URIs for data items given returned in the JSON block. `"self"` is the link you just called. `"first"` is a link to another resource provided by this webapp. (These links don't actually work.)
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

