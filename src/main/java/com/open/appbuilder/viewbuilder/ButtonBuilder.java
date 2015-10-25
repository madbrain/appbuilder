package com.open.appbuilder.viewbuilder;

import java.util.HashMap;
import java.util.Map;

public class ButtonBuilder implements ElementBuilder {

    private static final Map<ButtonType, String> typeMap = new HashMap<>();

    static {
        typeMap.put(ButtonType.DEFAULT, "btn-default");
        typeMap.put(ButtonType.PRIMARY, "btn-primary");
        typeMap.put(ButtonType.WARNING, "btn-warning");
    }

    private ButtonType type = ButtonType.DEFAULT;
    private String onClick;
    private String isDisabled;
    private String label;

    public ButtonBuilder(String label) {
        this.label = label;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<button type=\"button\" class=\"");
        buildClass(builder, "btn");
        builder.append("\"");
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
        if (!isFirst) {
            builder.append(" ");
        }
        builder.append(typeMap.get(type));
    }

    public ButtonBuilder onClick(String onClick) {
        this.onClick = onClick;
        return this;
    }

    public ButtonBuilder type(ButtonType type) {
        this.type = type;
        return this;
    }

    public ButtonBuilder disabled(String isDisabled) {
        this.isDisabled = isDisabled;
        return this;
    }
}
