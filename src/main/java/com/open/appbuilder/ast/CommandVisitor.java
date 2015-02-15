package com.open.appbuilder.ast;

public interface CommandVisitor {

	void visitEnum(EnumCommand enumCommand);

	void visitEntity(EntityCommand entityCommand);

	void visitAssign(AssignCommand assignCommand);

	void visitBind(BindCommand bindCommand);

}
