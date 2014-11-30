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
 * The EL expression for choosing the collection is in the @Items annotation.
 */

//@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface _Embedded {
	Items[] links() default {};
	Class<? extends Object> linkset() default Object.class;
}
