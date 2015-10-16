package com.open.appbuilder.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Entity {

    JSONArray findAll();

    void save(JSONObject object);

}
