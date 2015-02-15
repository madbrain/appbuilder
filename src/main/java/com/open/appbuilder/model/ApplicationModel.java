package com.open.appbuilder.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.open.appbuilder.type.Type;

public class ApplicationModel {

	private Map<String, ScreenModel> screens = new HashMap<String, ScreenModel>();
	private Map<String, Type> types = new HashMap<>();

	public ScreenModel getScreen(String name) {
		return screens.get(name);
	}

	public void addScreen(ScreenModel screenModel) {
		this.screens.put(screenModel.getName(), screenModel);
	}

	public Collection<ScreenModel> getScreens() {
		return screens.values();
	}

	public Type getType(String name) {
		return types.get(name);
	}

	public void addType(String name, Type enumType) {
		this.types.put(name, enumType);
	}

}
