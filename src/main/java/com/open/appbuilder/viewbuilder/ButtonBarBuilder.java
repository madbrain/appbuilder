package com.open.appbuilder.viewbuilder;

public class ButtonBarBuilder implements ElementBuilder {

    private ButtonBuilder[] buttons;

    public ButtonBarBuilder(ButtonBuilder... buttons) {
        this.buttons = buttons;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<div class=\"btn-toolbar\" role=\"toolbar\">\n");
        builder.append("<div class=\"btn-group\" role=\"group\">\n");
        for (ButtonBuilder button : buttons) {
            button.build(builder);
        }
        builder.append("</div>\n");
        builder.append("</div>\n");
    }

}
