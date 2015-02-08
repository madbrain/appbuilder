package com.open.appbuilder.ast;

public abstract class Widget extends AstNode {

	public Widget(Span span) {
		super(span);
	}

	public abstract void visit(WidgetVisitor visitor);

}
