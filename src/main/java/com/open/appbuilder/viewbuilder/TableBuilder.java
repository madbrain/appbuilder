package com.open.appbuilder.viewbuilder;

public class TableBuilder implements ElementBuilder {

    private TableColumnBuilder[] columns;
    private String reapeatExpr;
    private String onClick;
    private String cssClass;

    public TableBuilder(String reapeatExpr, TableColumnBuilder... columns) {
        this.reapeatExpr = reapeatExpr;
        this.columns = columns;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("<table class=\"table table-striped table-hover\">\n");

        builder.append("<tr>\n");
        for (TableColumnBuilder columnBuilder : columns) {
            columnBuilder.buildHeader(builder);
        }
        builder.append("</tr>\n");
        builder.append("<tr ng-repeat=\"").append(reapeatExpr).append("\"");
        if (onClick != null) {
            builder.append(" ng-click=\"").append(onClick).append("\"");
        }
        if (cssClass != null) {
            builder.append(" ng-class=\"").append(cssClass).append("\"");
        }
        builder.append(">\n");
        for (TableColumnBuilder columnBuilder : columns) {
            columnBuilder.buildRow(builder);
        }
        builder.append("</tr>\n");
        builder.append("</table>\n");
    }

    public TableBuilder onClick(String onClick) {
        this.onClick = onClick;
        return this;
    }

    public TableBuilder cssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }
}
