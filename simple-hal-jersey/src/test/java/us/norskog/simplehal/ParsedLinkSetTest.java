package us.norskog.simplehal;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;

import us.norskog.simplehal.ParsedLinkSet;

public class ParsedLinkSetTest {

	@Test
	public void test() throws NoSuchMethodException, SecurityException {
		Endpoint e = new Endpoint();
		Method getLinks = e.getClass().getMethod("getLinks", new Class[0]);
		Method getAll = e.getClass().getMethod("getAll", new Class[0]);
		Annotation[] linksAnnos = getLinks.getAnnotations();
		Annotation[] allAnnos = getAll.getAnnotations();
		ParsedLinkSet parsedLinkSetLinks = new ParsedLinkSet(linksAnnos);
		ParsedLinkSet parsedLinkSetAll = new ParsedLinkSet(allAnnos);
		// TODO: more here I guess
	}

}
