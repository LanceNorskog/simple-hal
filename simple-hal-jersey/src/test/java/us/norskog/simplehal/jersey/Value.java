package us.norskog.simplehal.jersey;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Value {
	String first = "one";
	String second = "two";
	String[] array = {"abc", "def"};
	List<String> list = new ArrayList<String>();
	Map<String, Integer> map = new LinkedHashMap<String, Integer>();

	public Value() {
		list.add("ten");
		list.add("eleven");
		map.put("100", 100);
		map.put("101", 101);
	}
	
	public String getFirst() { return first; }
	public String getSecond() { return second;}
	public String[] getArray() { return array;}

	public List<String> getList() { return list; }
	public Map<String, Integer> getMap() {return map;}

}
