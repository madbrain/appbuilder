package com.open.appbuilder.bootstrap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;

import com.open.appbuilder.Materializer;
import com.open.appbuilder.ast.StringExpr;
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

    public static class BootstrapHelper {

        public boolean isInputText(WidgetModel widget) {
            return widget.getType() == INPUT_TEXT;
        }

        public boolean isCombo(WidgetModel widget) {
            return widget.getType() == COMBO;
        }

        public boolean isButton(WidgetModel widget) {
            return widget.getType() == BUTTON;
        }
    }

    public static final int MAX_LINE_LENGTH = 12;

    public static final WidgetTypeModel LABEL = new WidgetTypeModel();

    public static final WidgetTypeModel BUTTON = new WidgetTypeModel() {
        @Override
        public void analyseArguments(FieldModel field, List<StringExpr> arguments) {
            // XXX add error reporting
            field.setProperty("label", arguments.get(0).getValue());
        }
    };
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
            computeScreenSize(screen);
            generateScreen(screen);
        }
    }

    private void computeScreenSize(ScreenModel screen) {
        for (WidgetLineModel line : screen.getLines()) {
            int count = 0;
            int labelCount = 0;
            boolean hasFirstLabel = false;
            for (int i = 0; i < line.getWidgets().size(); ++i) {
                WidgetModel widget = line.getWidgets().get(i);
                ++count;
                if (widget.hasLabel()) {
                    ++labelCount;
                    if (i == 0) {
                        hasFirstLabel = true;
                    }
                }
            }
            double step = (MAX_LINE_LENGTH - 2 - labelCount) / (double) count;
            if (hasFirstLabel) {
                step = (MAX_LINE_LENGTH - 2 - (labelCount - 1)) / (double) count;
            }
            double sum = 0;
            int intSize = 0;
            for (int i = 0; i < line.getWidgets().size(); ++i) {
                WidgetModel widget = line.getWidgets().get(i);
                sum += step;
                int size = (int) Math.floor(sum - intSize + 0.5d);
                intSize += size;
                if (i == 0) {
                    if (widget.hasLabel()) {
                        ((FieldModel) widget).getLabel().setProperty("sizeClass", "col-sm-2");
                        widget.setProperty("sizeClass", "col-sm-" + size);
                    } else {
                        widget.setProperty("sizeClass", "col-sm-offset-2 col-sm-" + size);
                    }
                } else {
                    if (widget.hasLabel()) {
                        ((FieldModel) widget).getLabel().setProperty("sizeClass", "col-sm-1");
                    }
                    widget.setProperty("sizeClass", "col-sm-" + size);
                }
            }
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
        context.put("helper", new BootstrapHelper());
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
