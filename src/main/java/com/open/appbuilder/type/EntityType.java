package com.open.appbuilder.type;

import java.util.Map;

public class EntityType extends Type {

	private String name;
	private Map<String, EntityField> fields;

	public EntityType(String name, Map<String, EntityField> fields) {
		this.name = name;
		this.fields = fields;
	}

	public String getName() {
		return name;
	}

	public Map<String, EntityField> getFields() {
		return fields;
	}

}
