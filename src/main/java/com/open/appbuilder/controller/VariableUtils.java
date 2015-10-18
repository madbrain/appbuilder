package com.open.appbuilder.controller;

public final class VariableUtils {
    private VariableUtils() {
    }

    public static String uncapitalize(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}
