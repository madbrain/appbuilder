package com.open.appbuilder.model;

import java.util.HashMap;
import java.util.Map;

public abstract class WidgetModel {

    private String name;
    private WidgetTypeModel type;
    private Map<String, String> extraArgs = new HashMap<>();

    public WidgetModel(String name, WidgetTypeModel type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public WidgetTypeModel getType() {
        return type;
    }

    public boolean hasLabel() {
        return false;
    }

    public void setProperty(String key, String value) {
        this.extraArgs.put(key, value);
    }

    public String property(String key) {
        return extraArgs.get(key);
    }

}
