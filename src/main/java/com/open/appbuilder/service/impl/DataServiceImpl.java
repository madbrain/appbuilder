package com.open.appbuilder.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.open.appbuilder.service.DataService;
import com.open.appbuilder.service.Entity;

/**
 * MOCK to be modified with data metamodel and real database access
 */
@Service
public class DataServiceImpl implements DataService {

    // TODO: use concurent object
    private Map<String, EntityImpl> elements = new HashMap<>();

    @PostConstruct
    protected void initialize() {
        elements.put("ingredients", new EntityImpl(Arrays.asList(
                makeIngredientObject(0, "Banane", "PIECE", 0.0, "Martinique", 0.5),
                makeIngredientObject(1, "Limonade", "LITRE", 0.0, "", 1),
                makeIngredientObject(2, "Sel", "PINCEE", 0.0, "", 0.1),
                makeIngredientObject(3, "Ricard", "LITRE", 45.0, "France", 30.0))));
    }

    @Override
    public Entity getEntity(String entityName) {
        return elements.get(entityName);
    }

    private JSONObject makeIngredientObject(int id, String name, String unite,
            double tauxAlcool, String origine, double coutUnitaire) {
        JSONObject object = new JSONObject();
        object.put("@id", id);
        object.put("name", name);
        object.put("unite", unite);
        object.put("tauxAlcool", tauxAlcool);
        object.put("origine", origine);
        object.put("coutUnitaire", coutUnitaire);
        return object;
    }

}
