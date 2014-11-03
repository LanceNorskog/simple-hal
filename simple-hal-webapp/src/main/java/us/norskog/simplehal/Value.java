package us.norskog.simplehal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Value {
	String first = "one";
	String second = "two";
	String[] array = {"abc", "def"};
	List<String> list = new ArrayList<String>();
	Map<String, Integer> map = new HashMap<String, Integer>();
	Boolean doFirst = true;
	Boolean doArray = true;
	Boolean doList = true;
	Boolean doMap = true;

	public Value() {
		list.add("ten");
		list.add("eleven");
		map.put("100", 100);
		map.put("101", 101);
	}

	public void setDoFirst(Boolean doFirst) {
		this.doFirst = doFirst;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Map<String, Integer> getMap() {
		return map;
	}

	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}

	public String getFirst() {
		return first;
	}

	public Boolean getDoFirst() {
		return doFirst;
	}

	public Boolean getDoArray() {
		return doArray;
	}

	public void setDoArray(Boolean doArray) {
		this.doArray = doArray;
	}

	public Boolean getDoList() {
		return doList;
	}

	public void setDoList(Boolean doList) {
		this.doList = doList;
	}

	public Boolean getDoMap() {
		return doMap;
	}

	public void setDoMap(Boolean doMap) {
		this.doMap = doMap;
	}
	
}
