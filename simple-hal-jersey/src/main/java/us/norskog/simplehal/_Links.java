package us.norskog.simplehal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Top-level container for Link annotations.
 * 
 * Specify HAL hyperlinks to add to your endpoint's return value.
 * endpoint must return a structure, map<String,Object>, array,
 * or JSON in a string.
 * 
 * @author lance
 *
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface _Links {
	Link[] links();
	String doc() default "";
}
