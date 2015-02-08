package com.open.appbuilder.model;

public class FieldModel extends WidgetModel {

	private LabelWidgetModel label;

	public FieldModel(LabelWidgetModel label, String name, WidgetTypeModel type) {
		super(name, type);
		this.label = label;
	}
	
	public LabelWidgetModel getLabel() {
		return label;
	}

	@Override
	public boolean hasLabel() {
		return label != null;
	}
}
