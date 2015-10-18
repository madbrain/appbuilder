package com.open.appbuilder.controller;

public class MenuItem {

    private String url;
    private String label;
    private boolean isActive;

    public static class Builder {

        private MenuItem item = new MenuItem();

        public MenuItem.Builder url(String url) {
            item.url = url;
            return this;
        }

        public MenuItem.Builder active() {
            item.isActive = true;
            return this;
        }

        public MenuItem.Builder label(String label) {
            item.label = label;
            return this;
        }

        public MenuItem build() {
            return item;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public boolean getIsActive() {
        return isActive;
    }
}