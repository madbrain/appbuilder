package com.open.appbuilder.ast;

import java.util.List;

public class WidgetDefinition extends Widget {

    private Identifier type;
    private Identifier name;
    private List<StringExpr> arguments;

    public WidgetDefinition(Span span, Identifier type,
            Identifier name, List<StringExpr> arguments) {
        super(span);
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

    public List<StringExpr> getArguments() {
        return arguments;
    }

    @Override
    public void visit(WidgetVisitor visitor) {
        visitor.visitWidgetDefinition(this);
    }

}
