package com.open.appbuilder.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private Map<String, List<JSONObject>> elements = new HashMap<>();

    @PostConstruct
    protected void initialize() {
        elements.put("ingredients", Arrays.asList(
                makeIngredientObject("Banane", "PIECE", 0.0, "Martinique", 0.5),
                makeIngredientObject("Limonade", "LITRE", 0.0, "", 1),
                makeIngredientObject("Sel", "PINCEE", 0.0, "", 0.1),
                makeIngredientObject("Ricard", "LITRE", 45.0, "France", 30.0)));
    }

    @Override
    public Entity getEntity(String entityName) {
        List<JSONObject> entities = elements.get(entityName);
        if (entities == null) {
            return null;
        }
        return new EntityImpl(entities);
    }

    private JSONObject makeIngredientObject(String name, String unite,
            double tauxAlcool, String origine, double coutUnitaire) {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("unite", unite);
        object.put("tauxAlcool", tauxAlcool);
        object.put("origine", origine);
        object.put("coutUnitaire", coutUnitaire);
        return object;
    }

}
