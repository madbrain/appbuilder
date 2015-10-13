package com.open.appbuilder.controller;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

import com.open.appbuilder.service.DataService;
import com.open.appbuilder.service.Entity;

@Controller
public class GenericRestController {

    private DataService databaseService;

    @Autowired
    public GenericRestController(DataService databaseService) {
        this.databaseService = databaseService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rest/{entityName}/**")
    public ResponseEntity<String> screen(@PathVariable String entityName, ServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        Entity entity = databaseService.getEntity(entityName);

        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(entity.findAll().toString(), headers, HttpStatus.OK);
    }
}
