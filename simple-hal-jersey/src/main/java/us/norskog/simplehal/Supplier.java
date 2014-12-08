package us.norskog.simplehal;

import java.util.Map;

/**
 * Supply links for an object. Used in @_Links.supplier and @_Embedded.supplier.
 * SimpleHAL calls getLink() to produce links OR uses @_Links and @_Embedded annotations
 * declared on getLink() method. This allows subclasses of Hyper to declare
 * annotations once instead of multiple times.
 * 
 * <br/>
 * The Links annotations are really noisy and need to be duplicated.
 * The Jersey solution of embedding them on returned BOs is limited.
 * 
 * @author lance
 *
 */
public abstract class Supplier {

	public abstract Map<String, ? extends Object> getLink(Object base);
}
