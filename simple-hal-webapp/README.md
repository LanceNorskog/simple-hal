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
      "href": "/simplehal/embedded?id=one"
    },
    "self": {
      "title": "Self",
      "href": "/simplehal/embedded"
    }
  }
  ```
```
curl -q -H "Accept: application/hal+json" http://localhost:8080/simplehal/embedded 
```
SimpleHAL will adds these elements to the returned JSON block:
```
"_links": {
    "first": {
      "title": "First",
      "href": "/simplehal/embedded?id=one"
    },
    "self": {
      "title": "Self",
      "href": "/simplehal/embedded"
    }
  },
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

