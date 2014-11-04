package us.norskog.simplehal.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse and save parts of a hyperlink spec
 */
public class Parser {
    List<Expression> expressions = new ArrayList<Expression>();

    public Parser(String spec) {
        StringBuilder sb = new StringBuilder();
      // build list of parts that are either Strings or Expressions
        // need inside/outside boolean to decide which.
        boolean el = false;
        for(int i = 0; i < spec.length(); i++) {
            char ch = spec.charAt(i);
            if (el) {
                if (ch == '}') {
                    if (sb.length() > 2) {
                    	sb.append("}");
                        expressions.add(new Expression(sb.toString(), true));
                        sb.setLength(0);
                    } else {
                    	expressions.add(new Expression("#NULL", false));
                    	return;
                    }
                    el = false;
                } else {
                    sb.append(ch);
                }
            } else {
                if (ch == '$') {
                    if (i + 1 == spec.length() || ! (spec.charAt(i + 1) == '{')) {
                    	expressions.add(new Expression("#NULL", false));
                    	return;
                    }
                    i++;
                    if (sb.length() > 0) {
                        expressions.add(new Expression(sb.toString(), false));
                        sb.setLength(0);
                    }
                    sb.append("${");
                    el = true;
                } else if (ch == '{' || ch == '}') {
                	expressions.add(new Expression("#NULL", false));
                	return;
                } else {
                        sb.append(ch);
                }
            }
        }
        if (el)
            expressions.add(new Expression("#NULL", false));
        else if (sb.length() > 0 || expressions.size() == 0)
            expressions.add(new Expression(sb.toString(), false));
    }

     public List<Expression> getExpressions() {
        return expressions;
    }
}

