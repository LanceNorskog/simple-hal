package us.norskog.simplehal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// @Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkSet {
	Link[] links();
}
