package us.norskog.simplehal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * Set of links to be included in HAL block.
 * 'check' expression decides whether to include this block
 *
 */

// @Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkSet {
	/**
	 * Link specifications to be created and returned in _links or _embedded block
	 */
	Link[] links();
	
	/**
	 * Check expression. If 0 or 'false' or NULL or empty string, do not include this entire linkSet.
	 */
	String check() default "true";
}
