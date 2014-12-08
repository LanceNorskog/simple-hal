### SimpleHAL API
There are 5 classes in the API: 4 annotations and one abstract class.

#### **@_Links**

The `@_Links` annotation specifies a collection of top-level links.

**@Link** 

The `@Link` annotation specifies one link. It is only used inside `@_Links`.

**@_Embedded**

The `@_Embedded` annotations specifies set of links for items in a collections.

**@Items**

The `@Items` annotation specifies a set of links for an item in a collection.
It is only used inside @_Embedded annotations.

**Supplier**

This class provides two features:
* It can be used instead of an annotation to supply links to `@_Links` and `@_Embedded`.
* It is used as a template feature for annotations, and allows annotations to be re-used across multiple Jersey endpoints.
