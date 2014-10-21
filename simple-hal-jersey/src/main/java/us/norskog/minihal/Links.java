package us.norskog.minihal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify HAL hyperlinks to add to your endpoint's return value.
 * endpoint must return a structure, map<String,Object>, array,
 * or JSON in a string.
 * 
 * @author lance
 *
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Links {
	LinkSet linkset();
	Embedded[] embedded() default {};
}
