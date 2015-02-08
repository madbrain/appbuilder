package com.open.appbuilder.model;

import java.util.ArrayList;
import java.util.List;

public class GroupModel extends WidgetModel {

	private List<WidgetModel> widgets = new ArrayList<>();

	public GroupModel(String groupName) {
		super(groupName, null);
	}

	public void add(WidgetModel widget) {
		this.widgets.add(widget);
	}

}
