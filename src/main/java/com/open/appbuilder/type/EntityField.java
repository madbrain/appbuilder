package com.open.appbuilder.type;

public class EntityField {

    private String name;
    private Type type;

    public EntityField(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

}
