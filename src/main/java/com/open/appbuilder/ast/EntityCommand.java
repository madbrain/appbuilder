package com.open.appbuilder.ast;

import java.util.List;

public class EntityCommand extends Command {

    private Identifier name;
    private List<Field> fields;

    public EntityCommand(Span span, Identifier name, List<Field> fields) {
    	super(span);
        this.name = name;
        this.fields = fields;
    }

    public Identifier getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields;
    }

	@Override
	public void visit(CommandVisitor visitor) {
		visitor.visitEntity(this);
	}

}
