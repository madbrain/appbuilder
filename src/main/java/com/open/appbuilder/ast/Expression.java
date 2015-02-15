package com.open.appbuilder.ast;

public abstract class Expression {

	private Span span;

	public Expression(Span span) {
		this.span = span;
	}
	
	public Span getSpan() {
		return span;
	}

}
