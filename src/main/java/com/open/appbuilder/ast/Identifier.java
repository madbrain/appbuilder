package com.open.appbuilder.ast;

public class Identifier extends AstNode {

    private String name;

    public Identifier(Span span, String name) {
    	super(span);
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
    	return name;
    }

}
