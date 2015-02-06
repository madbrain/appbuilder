package com.open.appbuilder;

import java.util.List;

public class WidgetDefinition extends Widget {

    private Identifier type;
    private Identifier name;
    private List<StringExpr> arguments;

    public WidgetDefinition(Identifier type, Identifier name, List<StringExpr> arguments) {
        this.type = type;
        this.name = name;
        this.arguments = arguments;
    }

    public Identifier getType() {
        return type;
    }

    public Identifier getName() {
        return name;
    }

}
