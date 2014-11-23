package us.norskog.simplehal;

import java.util.Map;

/**
 * Generate links.
 * 
 * @author lance
 *
 */
public abstract class Hyper<T> {

	public abstract Map<String, ? extends Object> getLink(T base);
}
