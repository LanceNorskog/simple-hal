package us.norskog.simplehal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// @Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Link {
	String rel();
	String title() default "";
	String href();
	String[] more() default {};
	String check() default "";
}
