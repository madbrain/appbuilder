package com.open.appbuilder.ast;

import java.util.List;

public class ObjectExpression extends Expression {

    private Identifier name;
    private List<Expression> arguments;

    public ObjectExpression(Span span, Identifier name, List<Expression> arguments) {
    	super(span);
    	this.name = name;
    	this.arguments = arguments;
    }

    public Identifier getName() {
        return name;
    }

    public List<Expression> getArguments() {
        return arguments;
    }
}
