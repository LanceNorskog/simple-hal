package us.norskog.simplehal;

/**
 * 
 * Return set of links for each member of a collection in the response object.
 * 'items' expression provides a value or sequence of values.
 * A set of links is created with each item visible to the link value expressions.
 * 'check' expression decides whether to include this block.
 */

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
	
	/**
	 * Check expression. If 0 or 'false' or NULL or empty string, do not include this entire linkSet.
	 */
	String check() default "true";
}
