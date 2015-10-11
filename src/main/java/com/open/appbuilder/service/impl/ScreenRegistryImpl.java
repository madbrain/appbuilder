package com.open.appbuilder.service.impl;

import org.springframework.stereotype.Service;

import com.open.appbuilder.model.ScreenModel;
import com.open.appbuilder.service.ScreenRegistry;

@Service
public class ScreenRegistryImpl implements ScreenRegistry {

    @Override
    public ScreenModel get(String screenName) {
        ScreenModel screen = new ScreenModel("tutu");
        // mock screen
        return screen;
    }

}
