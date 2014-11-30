package us.norskog.simplehal;

import java.util.Map;

/**
 * Generate links for an object.
 * OR, serve as carrier for Links annotations.
 * <br/>
 * The Links annotations are really noisy and need to be duplicated.
 * The Jersey solution of embedding them on returned BOs is limited.
 * 
 * @author lance
 *
 */
public abstract class Hyper {

	public abstract Map<String, ? extends Object> getLink(Object base);
}
