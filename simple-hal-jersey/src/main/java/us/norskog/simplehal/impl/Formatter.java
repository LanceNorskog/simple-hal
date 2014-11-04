package us.norskog.simplehal.impl;

import java.util.Map;

/**
 * Format an evaluated Map tree into hyperlink format.
 * 
 * Factored out to allow different formats: Spring, HAL library, whatever.
 * 
 * @author lance
 *
 */

public interface Formatter {
	
	public Map<String, Object> format(Map<String, Object> response, LinksetMap builtLinks, 
			EmbeddedMap builtEmbedded);

}
