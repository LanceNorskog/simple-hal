package us.norskog.simplehal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Top-level container for Link annotations.
 * 
 * Specify HAL hyperlinks to add to your endpoint's return value.
 * endpoint may return a structure, map<String,Object>, array,
 * or JSON in a string. If it returns a structure or map<String,Object>
 * these links will be added. 
 * 
 * @author lance
 *
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface _Links {
	/**
	 * Array of specifications for a top-level link
	 */
	Link[] links() default {};
	/**
	 * Array of external Hyper classes that create sets of top-level links
	 */
	Class<? extends Supplier>[] linkset() default {};
}
