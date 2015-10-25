package com.open.appbuilder.viewbuilder;

public class TableColumnBuilder {

    private String label;
    private String model;

    public TableColumnBuilder(String label, String model) {
        this.label = label;
        this.model = model;
    }

    public void buildHeader(StringBuilder builder) {
        builder.append("<th>").append(label).append("</th>\n");
    }

    public void buildRow(StringBuilder builder) {
        builder.append("<td>{{").append(model).append("}}</td>\n");
    }

}
