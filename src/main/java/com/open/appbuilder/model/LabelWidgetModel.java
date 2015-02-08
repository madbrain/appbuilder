package com.open.appbuilder.model;

public class LabelWidgetModel extends WidgetModel {

	private String content;

	public LabelWidgetModel(String content, WidgetTypeModel type) {
		super(null, type);
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

}
