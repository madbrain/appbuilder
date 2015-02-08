package com.open.appbuilder.ast;

public interface WidgetVisitor {

	void visitLabelWidget(LabelWidget labelWidget);

	void visitWidgetDefinition(WidgetDefinition widgetDefinition);

	void visitWidgetGroup(WidgetGroup widgetGroup);

}
