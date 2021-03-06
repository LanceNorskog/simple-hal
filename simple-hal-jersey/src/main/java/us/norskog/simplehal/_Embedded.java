package us.norskog.simplehal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Top-level container for Embedded link sets.
 * 
 * '_embedded' links are sets of links generated for an n-ary EL expression
 * You can select an array or map object via an EL, and a set of links will
 * be generated from the templates. The templates can fetch the n'th position
 * and value from the array or map. EL expressions see these as item.key and item.value.
 *
 * The EL expression for choosing the collection is in the <i>items<i> string. 
 * 
 * <i>links</i> and <i>linkset</i> can both be used.
 * <i>links</i> and <i>items</i> must both be specified. 
 * the <i>items</i> expression override the expression in <i>linkset</i>.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface _Embedded {
	/**
	 * Array of specifications for links per item found by <i>items</i> EL expression
	 */
	Items[] links() default {};
	/**
	 * External Hyper class that creates a set of links per item
	 * Should be array.
	 */
	Class<? extends Supplier>[] linkset() default {};
	/**
	 * EL expression which selects a collection of items from the returned values.
	 * Overrides <i>items</i> inside Hyper class.
	 */
	String items() default "";
}
