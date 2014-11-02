package us.norskog.simplehal;

import java.util.List;
import java.util.Map;

/**
 * Format an evaluated Map tree into hyperlink format.
 * 
 * @author lance
 *
 */

public interface Formatter {
	
	public Map<String, Object> format(Map<String, Object> response, LinksetMap builtLinks, 
			EmbeddedMap builtEmbedded);

}
