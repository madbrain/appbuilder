package com.open.appbuilder.ast;

public class Span {
	private int start;
	private int end;

	public Span(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public Span add(Span other) {
		return new Span(start, other.end);
	}
}
