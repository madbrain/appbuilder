package com.open.appbuilder.controller;

import java.util.Arrays;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

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
        return "index";
    }

    @RequestMapping("/js/app.js")
    public String appJS(Model model) {
        model.addAttribute("routes", new Routes.Builder()
                .route("ingredients", "IngredientListCtrl")
                .route("cocktails", "CocktailListCtrl")
                .defaultRoute("ingredients")
                .build());
        return "appJS";
    }

    @RequestMapping("/screen/{screenName}/**")
    public String screen(@PathVariable String screenName, ServletRequest request) {
        ScreenModel screenModel = screenRegistry.get(screenName);
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return "<p>Hello screen " + screenName + "! (" + path + ")</p>";
    }
}
