package com.open.appbuilder.parser;


public class ParseException extends Exception {

	private static final long serialVersionUID = -512216427778763995L;

	public ParseException(String msg) {
		super(msg);
	}

	public ParseException(Throwable e) {
		super(e);
	}
	
}
