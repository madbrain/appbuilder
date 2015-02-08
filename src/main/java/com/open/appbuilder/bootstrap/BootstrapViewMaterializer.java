package com.open.appbuilder.bootstrap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;

import com.open.appbuilder.Materializer;
import com.open.appbuilder.model.ApplicationModel;
import com.open.appbuilder.model.FieldModel;
import com.open.appbuilder.model.ScreenModel;
import com.open.appbuilder.model.WidgetLineModel;
import com.open.appbuilder.model.WidgetModel;
import com.open.appbuilder.model.WidgetTypeModel;

public class BootstrapViewMaterializer implements Materializer {
	
	private static class LabeledWidgetType extends WidgetTypeModel {
		@Override
		public boolean couldBeLabeled() {
			return true;
		}
	}
	
	public static final int MAX_LINE_LENGTH = 12;
	
	public static final WidgetTypeModel LABEL = new WidgetTypeModel();
	
	public static final WidgetTypeModel BUTTON = new WidgetTypeModel();
	public static final WidgetTypeModel INPUT_TEXT = new LabeledWidgetType();
	public static final WidgetTypeModel COMBO = new LabeledWidgetType();
	
	private static final Map<String, WidgetTypeModel> widgetTypes = new HashMap<>();
	
	static {
		widgetTypes.put("Button", BUTTON);
		widgetTypes.put("InputText", INPUT_TEXT);
		widgetTypes.put("Combo", COMBO);
	}
	
	@Override
	public WidgetTypeModel getWidgetType(String type) {
		return widgetTypes.get(type);
	}
	
	@Override
	public WidgetTypeModel getLabelType() {
		return LABEL;
	}

	@Override
	public boolean canAddFieldTo(FieldModel field, WidgetLineModel line) {
		int size = 0;
		for (WidgetModel widget : line.getWidgets()) {
			++size;
			if (widget.hasLabel()) {
				++size;
			}
		}
		return size < MAX_LINE_LENGTH;
	}

	public void generate(ApplicationModel model) {
		for (ScreenModel screen : model.getScreens()) {
			generateScreen(screen);
		}
	}

	private void generateScreen(ScreenModel screen) {
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		engine.init();
		
		Context context = new VelocityContext();
		context.put("model", screen);
		Template template = engine.getTemplate("/templates/html-bootstrap-main.vm", "UTF-8");
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(screen.getName() + ".html"), "UTF-8");
			template.merge(context, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
