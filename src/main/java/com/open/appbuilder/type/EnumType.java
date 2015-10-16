package com.open.appbuilder.type;

import java.util.List;

public class EnumType extends Type {

    private String name;
    private List<EnumElement> elements;

    public EnumType(String name, List<EnumElement> elements) {
        this.name = name;
        this.elements = elements;
    }

    public String getName() {
        return name;
    }

}
