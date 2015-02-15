package com.open.appbuilder.ast;

public class BindCommand extends Command {

    private Expression left;
    private Expression right;

    public BindCommand(Span span, Expression left, Expression right) {
    	super(span);
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

	@Override
	public void visit(CommandVisitor visitor) {
		visitor.visitBind(this);
	}

}
