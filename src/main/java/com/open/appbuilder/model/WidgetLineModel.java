package com.open.appbuilder.model;

import java.util.ArrayList;
import java.util.List;

public class WidgetLineModel {

	private ScreenModel screenModel;
	private List<WidgetModel> widgets = new ArrayList<>();

	public WidgetLineModel(ScreenModel screenModel) {
		this.screenModel = screenModel;
	}

	public void add(WidgetModel widgetModel) {
		this.widgets.add(widgetModel);
		screenModel.addWidget(widgetModel);
	}

	public WidgetModel getLast() {
		if (widgets.size() > 0) {
			return widgets.get(widgets.size()-1);
		}
		return null;
	}

	public void replaceLast(WidgetModel widget) {
		widgets.set(widgets.size()-1, widget);
	}

	public List<WidgetModel> getWidgets() {
		return widgets;
	}

}
