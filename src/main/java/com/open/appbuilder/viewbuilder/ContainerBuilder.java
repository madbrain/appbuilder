package com.open.appbuilder.viewbuilder;

public class ContainerBuilder implements ElementBuilder {

    private ElementBuilder[] elements;

    public ContainerBuilder(ElementBuilder... elements) {
        this.elements = elements;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<div class=\"container\">\n");
        for (ElementBuilder elementBuilder : elements) {
            elementBuilder.build(builder);
        }
        builder.append("</div>\n");
    }

}
