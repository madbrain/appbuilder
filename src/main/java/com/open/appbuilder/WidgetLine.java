package com.open.appbuilder;

import java.util.ArrayList;
import java.util.List;

public class WidgetLine {

    private List<Widget> widgets = new ArrayList<>();

    public WidgetLine(List<Widget> widgets) {
        this.widgets.addAll(widgets);
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

}
