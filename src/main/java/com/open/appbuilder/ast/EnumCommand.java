package com.open.appbuilder.ast;

import java.util.List;

public class EnumCommand extends Command {

    private Identifier name;
    private List<Identifier> elements;

    public EnumCommand(Span span, Identifier name, List<Identifier> elements) {
    	super(span);
        this.name = name;
        this.elements = elements;
    }

    public Identifier getName() {
        return name;
    }

    public List<Identifier> getElements() {
        return elements;
    }

	@Override
	public void visit(CommandVisitor visitor) {
		visitor.visitEnum(this);
	}

}
