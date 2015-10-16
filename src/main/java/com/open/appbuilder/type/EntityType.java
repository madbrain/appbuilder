package com.open.appbuilder.type;

import java.util.List;

public class EntityType extends Type {

    private String name;
    private List<EntityField> fields;

    public EntityType(String name, List<EntityField> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public List<EntityField> getFields() {
        return fields;
    }

}
