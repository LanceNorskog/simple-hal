package us.norskog.simplehal;


import org.junit.Assert;
import org.junit.Test;

import us.norskog.simplehal.Expression;
import us.norskog.simplehal.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lance on 10/8/14.
 */
public class ParserTest {

    @Test
    public void parseSimple() {
        Parser p = new Parser("");
        List<Expression> parts = p.getExpressions();
        Assert.assertEquals(1, parts.size());
        Assert.assertEquals("", parts.get(0).toString());
        p = new Parser("/");
        parts = p.getExpressions();
        Assert.assertEquals(1, parts.size());
        Assert.assertEquals("/", parts.get(0).toString());
        p = new Parser("/ab");
        parts = p.getExpressions();
        Assert.assertEquals(1, parts.size());
        Assert.assertEquals("/ab", parts.get(0).toString());
    }

    @Test
    public void parse1Expr() {
        Parser p = new Parser("${q}");
        List<Expression> parts = p.getExpressions();
        Assert.assertEquals(1, parts.size());
        Assert.assertEquals(Expression.class, parts.get(0).getClass());
        Assert.assertEquals("${q}", parts.get(0).toString());
        p = new Parser("${qr}");
        parts = p.getExpressions();
        Assert.assertEquals(1, parts.size());
        Assert.assertEquals(Expression.class, parts.get(0).getClass());
        Assert.assertEquals("${qr}", parts.get(0).toString());
        p = new Parser("${request.q}");
        parts = p.getExpressions();
        Assert.assertEquals(1, parts.size());
        Assert.assertEquals(Expression.class, parts.get(0).getClass());
        Assert.assertEquals("${request.q}", parts.get(0).toString());
        p = new Parser("/abc/${request.q}");
        parts = p.getExpressions();
        Assert.assertEquals(2, parts.size());
        Assert.assertEquals("/abc/", parts.get(0).toString());
        Assert.assertEquals(Expression.class, parts.get(1).getClass());
        Assert.assertEquals("${request.q}", parts.get(1).toString());
        p = new Parser("${request.q}/def");
        parts = p.getExpressions();
        Assert.assertEquals(2, parts.size());
        Assert.assertEquals("${request.q}", parts.get(0).toString());
        Assert.assertEquals(Expression.class, parts.get(0).getClass());
        Assert.assertEquals("/def", parts.get(1).toString());
        p = new Parser("/abc/${request.q}/def");
        parts = p.getExpressions();
        Assert.assertEquals(3, parts.size());
        Assert.assertEquals("/abc/", parts.get(0).toString());
        Assert.assertEquals(Expression.class, parts.get(1).getClass());
        Assert.assertEquals("${request.q}", parts.get(1).toString());
        Assert.assertEquals("/def", parts.get(2).toString());
    }

    @Test
    public void parse2Expr() {
        Parser p = new Parser("${request.q}${request.rows}");
        List<Expression> parts = p.getExpressions();
        Assert.assertEquals(2, parts.size());
        Assert.assertEquals(Expression.class, parts.get(0).getClass());
        Assert.assertEquals(Expression.class, parts.get(1).getClass());
        Assert.assertEquals("${request.q}", parts.get(0).toString());
        Assert.assertEquals("${request.rows}", parts.get(1).toString());
        p = new Parser("${request.q}/abc/${request.q}");
        parts = p.getExpressions();
        Assert.assertEquals(3, parts.size());
        Assert.assertEquals(Expression.class, parts.get(0).getClass());
        Assert.assertEquals("${request.q}", parts.get(0).toString());
        Assert.assertEquals("/abc/", parts.get(1).toString());
        Assert.assertEquals(Expression.class, parts.get(2).getClass());
        Assert.assertEquals("${request.q}", parts.get(2).toString());
        p = new Parser("${request.q}/def");
        parts = p.getExpressions();
        Assert.assertEquals(2, parts.size());
        Assert.assertEquals(Expression.class, parts.get(0).getClass());
        Assert.assertEquals("${request.q}", parts.get(0).toString());
        Assert.assertEquals("/def", parts.get(1).toString());
        p = new Parser("/abc/${request.q}/def");
        parts = p.getExpressions();
        Assert.assertEquals(3, parts.size());
        Assert.assertEquals("/abc/", parts.get(0).toString());
        Assert.assertEquals(Expression.class, parts.get(1).getClass());
        Assert.assertEquals("${request.q}", parts.get(1).toString());
        Assert.assertEquals("/def", parts.get(2).toString());
    }

    @Test
    public void parseFail() throws Exception {
        // empty string is ok but not empty EL
        illegal("${}");
        illegal("$");
        illegal("${");
        illegal("}");
        illegal("{}");
        illegal("${}/def");
        illegal("${request.q/def");
        illegal("$request.q}/def");
        illegal("{request.q}/def");
        illegal("request.q}/def");

        illegal("/abc/{request.q}");
        illegal("/abc/$request.q}");
        illegal("/abc/{request.q");
        illegal("/abc/request.q}");
      }

    private void illegal(String expr) throws Exception {
        try {
            Parser p = new Parser(expr);
            List<Expression> parts = p.getExpressions();
            for(Expression part: parts) {
            	if (part.toString().equals("#NULL"))
            		return;
            }
            throw new Exception("Expr should not parse or #NULL: " + expr);
        } catch (IllegalArgumentException e) {
        }
    }


    class Request {
        public Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("q", "monkeys");
            params.put("rows", "10");
            return params;
        }
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

}