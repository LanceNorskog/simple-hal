package us.norskog.simplehal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * Specification for one link. Components:
 *     rel: name of link
 *     title: visible text for link (no i18n yet)
 *     href: top-level link. user expected to maintain host part.
 *     more: more name/value pairs like title and href
 *     check: EL boolean expression for whether to include this link
 *
 *     title, href and more sets may include EL expressions which create any base type 
 *     The response object's getters are visible as response.thing
 *     For embedded links, the item variable is visible as item.key and item.value
 *     For maps, key and value are... key and value.
 *     For arrays/lists, key is array index and value is that entry in the array/list.
 *     For non-collections, key is 0 and the value is the item.
 *     
 *     This EL feature is what makes the string-based annotation useful.
 *     The EL interpreter is as safe as the getter methods on the returned object.
 *     It should probably be run under a separate classloader with a sandbox SecurityManager.
 */

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
	String[] href();
	
	/**
	 * More elements. Even-numbered array of strings.
	 * @return
	 */
	String[] more() default {};
	
	/**
	 * Check expression. If 0 or 'false' or NULL or empty string, do not include this link.
	 */
	String check() default "${true}";
}
