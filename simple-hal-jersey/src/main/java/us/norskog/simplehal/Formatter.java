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
	
	public Map<String, Object> format(Map<String, Object> response, List<Map<String, String>> builtLinks, 
			Map<String, List<List<Map<String, String>>>> builtEmbedded);

}
