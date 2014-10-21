package us.norskog.simplehal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import us.norskog.simplehal.Executor;

import java.util.*;

/**
 * All features of base Executor
 * including variable expansion
 * @author lance
 *
 */

public class ExecutorTest {
	Map<String,Object> map;
	Response response;
	Item item;
	Executor base = new Executor();

	@Before
	public void setUp() throws Exception {
		map = new HashMap<String,Object>();
		map.put("q", "monkeys");
		map.put("rows", 10);
		Item[] itemArray = new Item[2];
		itemArray[0] = new Item("A1");
		itemArray[1] = new Item("A2");
		map.put("itemArray", itemArray);
		map.put("itemList", new ArrayList<Item>());
		((List<Item>)map.get("itemList")).add(new Item("B1"));
		((List<Item>)map.get("itemList")).add(new Item("B2"));
		
		response = new Response();
		item = new Item("");
		setVars(base);
	}

	private void setVars(Executor executor) {
		executor.clear();
		executor.setVar("response", response);
		executor.setVar("item", item);
	}

	@Test
	public void response() {
		Object abc = base.evalExpr("${abc}");
		Assert.assertNull( abc);
		abc = base.evalExpr("${response.q}");
		Assert.assertEquals("monkeys", abc);
		abc = base.evalExpr("${response.rows}");
		Assert.assertEquals("10", abc.toString());
		abc = base.evalExpr("${response.rows * 2}");
		Assert.assertEquals("20", abc.toString());
	}

	@Test
	public void map() {
		base.clear();
		base.setVar("response", map);

		Object abc = base.evalExpr("${abc}");
		Assert.assertNull( abc);
		abc = base.evalExpr("${response.q}");
		Assert.assertEquals("monkeys", abc);
		abc = base.evalExpr("${response.rows}");
		Assert.assertEquals("10", abc.toString());
		abc = base.evalExpr("${response.rows * 2}");
		Assert.assertEquals("20", abc.toString());
		getItems();
	}

	@Test
	public void getItems() {
		List<Object> items = base.getList("${response.itemArray}");
		Assert.assertNotNull(items);
		Iterator<Object> iter = items.iterator();
		Assert.assertEquals("A1", ((Item) iter.next()).getValue().toString());
		Assert.assertEquals("A2", ((Item) iter.next()).getValue().toString());
		Assert.assertFalse(iter.hasNext());
		items = base.getList("${response.itemList}");
		Assert.assertNotNull(items);
		iter = items.iterator();
		Assert.assertEquals("B1", ((Item) iter.next()).getValue().toString());
		Assert.assertEquals("B2", ((Item) iter.next()).getValue().toString());
		Assert.assertFalse(iter.hasNext());
	}

//	@Test
//	public void fetchItems() {
//		List<Object> items = base.getList("${response.itemArray}");
//		Assert.assertNotNull(items);
//		Executor executor = new Executor();
//		Iterator<Object> iter = items.iterator();
//		Object first = iter.next();
//		base.clear();
//		base.setVar("item", first);
//		String a = base.evalExpr("${item.value}");
//		Assert.assertEquals("A1", a);
//		base.setVar("response", first);
//		a = base.evalExpr("${item.value}");
//		Assert.assertEquals("A2", a);
//		Assert.assertFalse(iter.hasNext());
//		items = base.getList("${response.itemList}");
//		Assert.assertNotNull(items);
//		iter = items.iterator();
//		base.setVar("item", first);
//		String b = base.evalExpr("${item.value}");
//		Assert.assertEquals("B1", b);
//		base.setVar("item", first);
//		b = base.evalExpr("${item.value}");
//		Assert.assertEquals("B2", b);
//		Assert.assertFalse(iter.hasNext());
//
//	}

//	@Test
//	public void fetchEmbedded() {
//		List<Object> itemList = base.getList("${response.itemArray}");
//		List<String> items = base.expandList("${response.itemArray}", response, "${item.value}");
//		Assert.assertNotNull(items);
//		Iterator<String> iter = items.iterator();
//		Assert.assertEquals("A1", iter.next());
//		Assert.assertEquals("A2", iter.next());
//		itemList = base.getList("${response.itemList}");
//		items = base.expandList(itemList, "${item.value}");
//		Assert.assertNotNull(items);
//		iter = items.iterator();
//		Assert.assertEquals("B1", iter.next());
//		Assert.assertEquals("B2", iter.next());
//	}
}

class Response {

	public String getQ() {
		return "monkeys";
	}

	public String getRows() {
		return "10";
	}

	public Item[] getItemArray() {
		Item[] items = {new Item("A1"), new Item("A2")};
		return items;
	}

	public List<Item> getItemList() {
		List<Item> list = new ArrayList<Item>();
		list.add(new Item("B1"));
		list.add(new Item("B2"));
		return list;
	}
}

class Item {
	private final String value;

	public Item(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

