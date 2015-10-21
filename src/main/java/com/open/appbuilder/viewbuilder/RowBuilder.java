package com.open.appbuilder.viewbuilder;

public class RowBuilder implements ElementBuilder {

    private ElementBuilder[] elements;

    public RowBuilder(ElementBuilder... elements) {
        this.elements = elements;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<div class=\"row top20\">\n");
        for (ElementBuilder elementBuilder : elements) {
            elementBuilder.build(builder);
        }
        builder.append("</div>\n");
    }

}
