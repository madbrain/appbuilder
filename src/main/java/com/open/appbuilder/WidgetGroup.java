package com.open.appbuilder;

import java.util.List;

public class WidgetGroup extends Widget {

    private Identifier name;
    private List<Widget> widgets;

    public WidgetGroup(Identifier name, List<Widget> widgets) {
        this.name = name;
        this.widgets = widgets;
    }

    public Identifier getName() {
        return name;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

}
