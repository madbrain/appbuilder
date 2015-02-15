package com.open.appbuilder.ast;

public class VarExpression extends Expression {

    private Identifier name;

    public VarExpression(Identifier name) {
    	super(name.getSpan());
        this.name = name;
    }

    public Identifier getName() {
        return name;
    }

}
