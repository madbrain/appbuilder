package com.open.appbuilder.ast;

public class Field {

    private Identifier name;
    private Identifier type;

    public Field(Identifier name, Identifier type) {
        this.name = name;
        this.type = type;
    }

    public Identifier getName() {
        return name;
    }

    public Identifier getType() {
        return type;
    }

}
