package com.open.appbuilder.model;

import java.util.List;

import com.open.appbuilder.ast.StringExpr;

public class WidgetTypeModel {

    public boolean couldBeLabeled() {
        return false;
    }

    public void analyseArguments(FieldModel field, List<StringExpr> arguments) {
    }

}
