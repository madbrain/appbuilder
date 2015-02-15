package com.open.appbuilder.ast;

public class FieldExpression extends Expression {

    private Expression expr;
    private Identifier fieldName;

    public FieldExpression(Span span, Expression expr, Identifier fieldName) {
    	super(span);
        this.expr = expr;
        this.fieldName = fieldName;
    }

}
