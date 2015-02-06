package com.open.appbuilder;

import java.util.List;

public class ObjectExpression extends Expression {

    private Identifier name;
    private List<Expression> arguments;

    public ObjectExpression(Identifier name, List<Expression> arguments) {
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
