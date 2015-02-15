package com.open.appbuilder.ast;

public abstract class Command {

	private Span span;
	
	public Command(Span span) {
		this.span = span;
	}

	public abstract void visit(CommandVisitor visitor);

	public Span getSpan() {
		return span;
	}
}
