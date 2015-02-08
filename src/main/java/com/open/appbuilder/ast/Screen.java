package com.open.appbuilder.ast;

import java.util.ArrayList;
import java.util.List;

public class Screen {

    private Identifier name;
    private List<WidgetLine> lines = new ArrayList<>();
    private List<Command> commands = new ArrayList<>();

    public Screen(Identifier name) {
        this.name = name;
    }

    public Identifier getName() {
        return name;
    }

    public List<WidgetLine> getWidgetLines() {
        return lines;
    }

    public List<Command> getCommands() {
        return commands;
    }

}
