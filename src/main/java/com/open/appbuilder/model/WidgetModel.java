package com.open.appbuilder.model;

public abstract class WidgetModel {

	private String name;
	private WidgetTypeModel type;

	public WidgetModel(String name, WidgetTypeModel type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public WidgetTypeModel getType() {
		return type;
	}

	public boolean hasLabel() {
		return false;
	}

}
