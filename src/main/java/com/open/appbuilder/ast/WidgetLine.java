package com.open.appbuilder.ast;

import java.util.ArrayList;
import java.util.List;

public class WidgetLine extends AstNode {

    private List<Widget> widgets = new ArrayList<>();

    public WidgetLine(AstList<Widget> widgets) {
    	super(widgets.getSpan());
        this.widgets.addAll(widgets);
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

}
