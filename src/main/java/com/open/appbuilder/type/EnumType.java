package com.open.appbuilder.type;

import java.util.Map;

public class EnumType extends Type {

	private String name;
	private Map<String, EnumElement> elements;

	public EnumType(String name, Map<String, EnumElement> elements) {
		this.name = name;
		this.elements = elements;
	}

	public String getName() {
		return name;
	}

}
