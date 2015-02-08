package com.open.appbuilder.ast;

import java.util.AbstractList;
import java.util.List;

public class AstList<T extends AstNode> extends AbstractList<T> {
	
	private Span span;
	private List<T> elements;

	public AstList(Span span, List<T> elements) {
		this.span = span;
		this.elements = elements;
	}

	@Override
	public T get(int index) {
		return elements.get(index);
	}

	@Override
	public int size() {
		return elements.size();
	}

	public Span getSpan() {
		return span;
	}

}
