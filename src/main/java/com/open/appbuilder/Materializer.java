package com.open.appbuilder;

import com.open.appbuilder.model.FieldModel;
import com.open.appbuilder.model.WidgetLineModel;
import com.open.appbuilder.model.WidgetTypeModel;

public interface Materializer {

	WidgetTypeModel getWidgetType(String type);

	WidgetTypeModel getLabelType();

	boolean canAddFieldTo(FieldModel field, WidgetLineModel line);

}
