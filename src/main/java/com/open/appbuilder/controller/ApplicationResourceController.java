package com.open.appbuilder.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.open.appbuilder.model.ScreenModel;
import com.open.appbuilder.service.ScreenRegistry;

@Controller
public class ApplicationResourceController {

    private ScreenRegistry screenRegistry;

    @Autowired
    public ApplicationResourceController(ScreenRegistry screenRegistry) {
        this.screenRegistry = screenRegistry;
    }

    @RequestMapping("/index.html")
    public String index(Model model) {
        model.addAttribute("title", "Bar à Cocktails");
        model.addAttribute("menuItems", Arrays.asList(
                new MenuItem.Builder().url("ingredients").label("Ingredients").active().build(),
                new MenuItem.Builder().url("cocktails").label("Cocktails").build()
                ));
        return "index.html";
    }

    @RequestMapping("/js/app.js")
    public String appJS(Model model) {
        model.addAttribute("routes", new Routes.Builder()
                .route("ingredients", "IngredientListCtrl")
                .route("cocktails", "CocktailListCtrl")
                .defaultRoute("ingredients")
                .build());
        return "app.js";
    }

    @RequestMapping("/views/{viewName}.html")
    @ResponseBody
    public String screen(@PathVariable String viewName) {
        ScreenModel screenModel = screenRegistry.get(viewName);
        return "<div class=\"container\">Hello view " + viewName + "!</div>";
    }
}
