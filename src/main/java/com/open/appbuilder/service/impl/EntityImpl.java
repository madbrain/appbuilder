package com.open.appbuilder.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.open.appbuilder.service.Entity;

public class EntityImpl implements Entity {

    private Map<Integer, JSONObject> entities = new HashMap<>();
    private int nextId;

    public EntityImpl(List<JSONObject> entities) {
        this.nextId = 0;
        for (JSONObject entity : entities) {
            int id = entity.getInt("@id");
            nextId = Math.max(nextId, id + 1);
            this.entities.put(id, entity);
        }
    }

    @Override
    public JSONArray findAll() {
        return new JSONArray(entities.values());
    }

    @Override
    public void save(JSONObject newObject) {
        if (newObject.isNull("@id")) {
            int newId = nextId++;
            newObject.put("@id", newId);
            this.entities.put(newId, newObject);
        } else {
            int id = newObject.getInt("@id");
            JSONObject obj = this.entities.get(id);
            if (obj == null) {
                throw new RuntimeException("Unknown entity with id=" + id);
            }
            for (String key : ((Set<String>) newObject.keySet())) {
                obj.put(key, newObject.get(key));
            }
        }
    }
}
