package com.open.appbuilder.controller;

import java.util.ArrayList;
import java.util.List;

public class Routes {

    public static class Route {

        private String url;
        private String controller;

        public Route(String url, String controller) {
            this.url = url;
            this.controller = controller;
        }

        public String getUrl() {
            return url;
        }

        public String getController() {
            return controller;
        }

        public String getControllerVar() {
            return VariableUtils.uncapitalize(controller);
        }

    }

    private List<Route> items = new ArrayList<>();
    private String defaultRouteUrl;

    public static class Builder {

        private Routes routes = new Routes();

        public Routes build() {
            return routes;
        }

        public Builder route(String url, String controller) {
            routes.items.add(new Route(url, controller));
            return this;
        }

        public Routes getRoutes() {
            return routes;
        }

        public Builder defaultRoute(String defaultRouteUrl) {
            routes.defaultRouteUrl = defaultRouteUrl;
            return this;
        }

    }

    public List<Route> getItems() {
        return items;
    }

    public String getDefaultRouteUrl() {
        return defaultRouteUrl;
    }

}
