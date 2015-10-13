package com.open.appbuilder.service.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.open.appbuilder.service.Entity;

public class EntityImpl implements Entity {

    private List<JSONObject> entities;

    public EntityImpl(List<JSONObject> entities) {
        this.entities = entities;
    }

    @Override
    public JSONArray findAll() {
        return new JSONArray(entities);
    }

}
