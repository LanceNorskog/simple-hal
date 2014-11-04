package us.norskog.simplehal.impl;

/**
 * Expression: evaluatable expression or simple string.
 */

class Expression {
    final private String expr;
    private boolean doEL;

    public Expression(String expr, boolean doEL) {
        this.expr = expr;
        this.doEL = doEL;
    }
    
    public void setDoEL(boolean doEl) {
    	this.doEL = doEl;
    }

    public Object eval(Executor executor) {
        if (! doEL)
            return expr;
        Object value = executor.evalExpr(expr);
        return value;
    }

    public String toString() {
        return expr;
    }
}
