package com.open.appbuilder.viewbuilder;

public class ButtonBuilder implements ElementBuilder {

    private boolean isDefault = false;
    private String onClick;
    private String isDisabled;
    private String label;

    public ButtonBuilder(String label, String onClick, boolean isDefault) {
        this(label, onClick, null, isDefault);
    }

    public ButtonBuilder(String label, String onClick, String isDisabled, boolean isDefault) {
        this.label = label;
        this.onClick = onClick;
        this.isDisabled = isDisabled;
        this.isDefault = isDefault;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<button type=\"button\" class=\"");
        buildClass(builder, "btn");
        if (onClick != null) {
            builder.append(" ng-click=\"").append(onClick).append("\"");
        }
        if (isDisabled != null) {
            builder.append(" ng-disabled=\"").append(isDisabled).append("\"");
        }
        builder.append(">").append(label).append("</button>\n");
    }

    private void buildClass(StringBuilder builder, String... classes) {
        boolean isFirst = true;
        for (String cssClass : classes) {
            if (!isFirst) {
                builder.append(" ");
            }
            builder.append(cssClass);
            isFirst = false;
        }
        if (isDefault) {
            if (!isFirst) {
                builder.append(" ");
            }
            builder.append("btn-default");
        }
    }
}
