package com.open.appbuilder.ast;

import java.util.List;

public class WidgetGroup extends Widget {

    private Identifier name;
    private List<Widget> widgets;

    public WidgetGroup(Span span, Identifier name, List<Widget> widgets) {
    	super(span);
        this.name = name;
        this.widgets = widgets;
    }

    public Identifier getName() {
        return name;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

	@Override
	public void visit(WidgetVisitor visitor) {
		visitor.visitWidgetGroup(this);
	}

}
