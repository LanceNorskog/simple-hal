package us.norskog.simplehal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// @Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Link {
	/**
	 * <b>rel</b> element
	 * @return
	 */
	String rel();

	/**
	 * <b>title</b> element (optional)
	 * @return
	 */
	String title() default "";
	
	/**
	 * <b>href</b> element (required)
	 * @return
	 */
	String href();
	
	/**
	 * More elements. String pairs or series of strings, either is ok.
	 * @return
	 */
	String[] more() default {};
	
	/**
	 * Check expression. If 0 or 'false' or NULL or empty string, do not include this link.
	 */
	String check() default "true";
}
