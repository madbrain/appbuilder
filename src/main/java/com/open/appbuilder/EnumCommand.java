package com.open.appbuilder;

import java.util.List;

public class EnumCommand extends Command {

    private Identifier name;
    private List<Identifier> elements;

    public EnumCommand(Identifier name, List<Identifier> elements) {
        this.name = name;
        this.elements = elements;
    }

    public Identifier getName() {
        return name;
    }

    public List<Identifier> getElements() {
        return elements;
    }

}
