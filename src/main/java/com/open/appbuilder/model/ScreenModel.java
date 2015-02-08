package com.open.appbuilder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenModel {

	private String name;
	private Map<String, WidgetModel> widgets = new HashMap<String, WidgetModel>();
	private List<WidgetLineModel> lines = new ArrayList<>();

	public ScreenModel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public WidgetModel getWidget(String name) {
		return widgets.get(name);
	}

	public void addWidget(WidgetModel widgetModel) {
		if (widgetModel.getName() != null) {
			widgets.put(widgetModel.getName(), widgetModel);
		}
	}

	public void addLine(WidgetLineModel line) {
		this.lines.add(line);
	}
	
	public List<WidgetLineModel> getLines() {
		return lines;
	}

}
