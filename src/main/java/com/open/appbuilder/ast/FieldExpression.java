package com.open.appbuilder.ast;

public class FieldExpression extends Expression {

    private Expression expr;
    private Identifier fieldName;

    public FieldExpression(Expression expr, Identifier fieldName) {
        this.expr = expr;
        this.fieldName = fieldName;
    }

}
