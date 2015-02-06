package com.open.appbuilder;

public class BindCommand extends Command {

    private Expression left;
    private Expression right;

    public BindCommand(Expression left, Expression right) {
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
