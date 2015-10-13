package com.open.appbuilder.controller;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.open.appbuilder.model.ScreenModel;
import com.open.appbuilder.service.ScreenRegistry;

@RestController
public class ScreenController {

    private ScreenRegistry screenRegistry;

    @Autowired
    public ScreenController(ScreenRegistry screenRegistry) {
        this.screenRegistry = screenRegistry;
    }

    @RequestMapping("/screen/{screenName}/**")
    public String screen(@PathVariable String screenName, ServletRequest request) {
        ScreenModel screenModel = screenRegistry.get(screenName);
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return "<p>Hello screen " + screenName + "! (" + path + ")</p>";
    }
}
