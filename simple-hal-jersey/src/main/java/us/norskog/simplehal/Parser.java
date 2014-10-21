package us.norskog.simplehal;

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
        boolean doEl = false;
        for(int i = 0; i < spec.length(); i++) {
            char ch = spec.charAt(i);
            if (el) {
                if (ch == '}') {
                    if (sb.length() > 2) {
                    	sb.append("}");
                        expressions.add(new Expression(sb.toString(), true));
                        sb.setLength(0);
                    } else {
                        throw new IllegalArgumentException();
                    }
                    el = false;
                } else {
                    sb.append(ch);
                }
            } else {
                if (ch == '$') {
                    if (i + 1 == spec.length() || ! (spec.charAt(i + 1) == '{'))
                        throw new IllegalArgumentException();
                    i++;
                    if (sb.length() > 0) {
                        expressions.add(new Expression(sb.toString(), false));
                        sb.setLength(0);
                    }
                    sb.append("${");
                    el = true;
                    doEl = true;
                } else if (ch == '{' || ch == '}') {
                    throw new IllegalArgumentException();
                } else {
                        sb.append(ch);
                }
            }
        }
        if (el)
            throw new IllegalArgumentException();
        if (sb.length() > 0 || expressions.size() == 0)
            expressions.add(new Expression(sb.toString(), false));
    }

     public List<Expression> getExpressions() {
        return expressions;
    }
}

