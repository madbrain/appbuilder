package com.open.appbuilder.ast;

import java.util.List;

public class EntityCommand extends Command {

    private Identifier name;
    private List<Field> fields;

    public EntityCommand(Identifier name, List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    public Identifier getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields;
    }

}
