package us.norskog.simplehal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 
 * Return set of links for each member of a collection in the response object.
 * 'items' expression provides a value or sequence of values.
 * A set of links is created with each item visible to the link value expressions.
 * EL expressions inside the @Link annotations can refer to item.key and item.value
 * which are the n'th position and value for the collection fetched by the items() EL.
 */

@Target(ElementType.METHOD)
public @interface Items {
	/**
	 * Name of embedded block
	 */
	String name();
	
	/**
	 * Expression used to fetch item value(s) for individual links. 
	 * Each link in an item found is visible to links expressions as 'item.key' and 'item.value'
	 */
	String items();
	
	/**
	 * Individual links generated from all items returned by expression
	 */
	LinkSet links();
}
