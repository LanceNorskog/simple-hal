package us.norskog.simplehal.impl;

import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implement EL actions for path, response and item.
 * Object is hard-created to eval one string.
 * User must create its own map of objects.
 *
 * TODO: does any of this really need optimizing?
 *
 */
public class Executor {

	private final ExpressionFactory factory;
	private SimpleContext context;
	Map<String,ValueExpression> valueExprs = new HashMap<String, ValueExpression>();

	public Executor() {
		factory = new de.odysseus.el.ExpressionFactoryImpl();
		clear();
	}
	
	public void clear() {
		context = new SimpleContext(new SimpleResolver());
		valueExprs.clear();
	}

	private ValueExpression setType(String name, Class<? extends Object> valueClass) {
		if (! valueExprs.containsKey(name)) {
			valueExprs.put(name, factory.createValueExpression(context, "#{" + name + "}", valueClass));
		}
		return valueExprs.get(name);
	}

	public void setVar(String name, Object value) {
		ValueExpression valueExpr = setType(name, value.getClass());
		valueExpr.setValue(context, value);
	}

	private Object eval(String given) {
		ValueExpression expr = factory.createValueExpression(context, given, Object.class);
		Object raw = null;
		try {
			raw = expr.getValue(context);
			return raw;
		} catch (javax.el.PropertyNotFoundException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object evalExpr(String single) {
		Object raw = eval(single);
		return raw;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getList(String selector) {
		Object raw = eval(selector);
		if (raw instanceof List)
			return (List<Object>) raw;
		if (raw.getClass().isArray()) {
			Object[] obs = (Object[]) raw;
			List<Object> list = new ArrayList<Object>();
			for(int i = 0; i < obs.length; i++) {
				list.add(i, obs[i]);
			}
			return list;
		}
		if (raw instanceof Collection) {
			List<Object> list = new ArrayList<Object>((Collection<Object>) raw);
			return list;
		}

		return Collections.emptyList();
	}

}
