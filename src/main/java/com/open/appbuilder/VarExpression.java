package com.open.appbuilder;

public class VarExpression extends Expression {

    private Identifier name;

    public VarExpression(Identifier name) {
        this.name = name;
    }

    public Identifier getName() {
        return name;
    }

}
