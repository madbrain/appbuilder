package com.open.appbuilder.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ApplicationModel {

	private Map<String, ScreenModel> screens = new HashMap<String, ScreenModel>();

	public ScreenModel getScreen(String name) {
		return screens.get(name);
	}

	public void addScreen(ScreenModel screenModel) {
		this.screens.put(screenModel.getName(), screenModel);
	}

	public Collection<ScreenModel> getScreens() {
		return screens.values();
	}

}
