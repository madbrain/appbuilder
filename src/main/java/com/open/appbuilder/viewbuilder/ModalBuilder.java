package com.open.appbuilder.viewbuilder;

public class ModalBuilder implements ElementBuilder {

    private String name;
    private String title;
    private ElementBuilder[] footerElements;

    public ModalBuilder(String name) {
        this.name = name;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<script type=\"text/ng-template\" id=\"").append(name).append("\">\n");
        builder.append("<div class=\"modal-header\">\n");
        builder.append("<h3 class=\"modal-title\">").append(title).append("</h3>\n");
        builder.append("</div>\n");
        builder.append("<div class=\"modal-body\">\n");

        builder.append("</div>\n");

        if (footerElements != null) {
            builder.append("<div class=\"modal-footer\">\n");
            for (ElementBuilder elementBuilder : footerElements) {
                elementBuilder.build(builder);
            }
            builder.append("</div>\n");
        }
        builder.append("</div");
        builder.append("</script>");
    }

    public ModalBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ElementBuilder footer(ElementBuilder... footerElements) {
        this.footerElements = footerElements;
        return this;
    }
}
