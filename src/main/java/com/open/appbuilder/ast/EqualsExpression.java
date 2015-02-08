package com.open.appbuilder.ast;

public class EqualsExpression extends Expression {

    private Expression left;
    private Expression right;

    public EqualsExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

}
