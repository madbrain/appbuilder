package com.open.appbuilder.ast;

public abstract class AstNode {
	
	private final Span span;
	
	public AstNode(Span span) {
		this.span = span;
	}
	
	public Span getSpan() {
		return span;
	}
}
