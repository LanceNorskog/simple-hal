package us.norskog.simplehal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * Set of links to be included in HAL block.
 *
 */

// @Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkSet {
	/**
	 * Link specifications to be created and returned in _links or _embedded block
	 */
	Link[] links();
}
