package com.open.appbuilder.semantic;

import java.util.Stack;

import com.open.appbuilder.ErrorReporter;
import com.open.appbuilder.Materializer;
import com.open.appbuilder.ast.LabelWidget;
import com.open.appbuilder.ast.Screen;
import com.open.appbuilder.ast.Span;
import com.open.appbuilder.ast.Spec;
import com.open.appbuilder.ast.Widget;
import com.open.appbuilder.ast.WidgetDefinition;
import com.open.appbuilder.ast.WidgetGroup;
import com.open.appbuilder.ast.WidgetLine;
import com.open.appbuilder.ast.WidgetVisitor;
import com.open.appbuilder.model.ApplicationModel;
import com.open.appbuilder.model.FieldModel;
import com.open.appbuilder.model.GroupModel;
import com.open.appbuilder.model.LabelWidgetModel;
import com.open.appbuilder.model.ScreenModel;
import com.open.appbuilder.model.WidgetLineModel;
import com.open.appbuilder.model.WidgetModel;
import com.open.appbuilder.model.WidgetTypeModel;

public class SpecSemantic {

    private final ErrorReporter reporter;
    private final Materializer materializer;

    public SpecSemantic(ErrorReporter reporter, Materializer materializer) {
        this.reporter = reporter;
        this.materializer = materializer;
    }

    public ApplicationModel analyse(Spec spec) {
        ApplicationModel model = new ApplicationModel();
        for (Screen screen : spec.getScreens()) {
            analyseScreen(screen, model);
        }
        return model;
    }

    private void analyseScreen(Screen screen, ApplicationModel model) {
        String screenName = screen.getName().getName();
        if (model.getScreen(screenName) != null) {
            reportError(screen.getName().getSpan(), "screen '" + screenName + "' already defined");
        } else {
            ScreenModel screenModel = new ScreenModel(screenName);
            model.addScreen(screenModel);
            for (WidgetLine widgetLine : screen.getWidgetLines()) {
                analyseWidgetLine(screenModel, widgetLine);
            }
        }
    }

    private class WidgetCreation implements WidgetVisitor {

        private ScreenModel screenModel;
        private WidgetLineModel line;

        private LabelWidgetModel lastLabel = null;
        private Stack<GroupModel> groups = new Stack<>();

        public WidgetCreation(ScreenModel screenModel, WidgetLineModel line) {
            this.screenModel = screenModel;
            this.line = line;
        }

        @Override
        public void visitLabelWidget(LabelWidget labelWidget) {
            if (lastLabel != null) {
                add(lastLabel);
            }
            lastLabel = new LabelWidgetModel(labelWidget.getContent(), materializer.getLabelType());
        }

        @Override
        public void visitWidgetDefinition(WidgetDefinition widgetDefinition) {
            String widgetType = widgetDefinition.getType().getName();
            WidgetTypeModel type = materializer.getWidgetType(widgetType);
            if (type == null) {
                reportError(widgetDefinition.getType().getSpan(), "widget type '" + widgetType + "' unknown");
            } else {
                String widgetName = widgetDefinition.getName().getName();
                if (screenModel.getWidget(widgetName) != null) {
                    reportError(widgetDefinition.getName().getSpan(), "widget '" + widgetName + "' already defined");
                } else {
                    LabelWidgetModel label = null;
                    if (lastLabel != null) {
                        if (type.couldBeLabeled()) {
                            label = lastLabel;
                        } else {
                            add(lastLabel);
                        }
                        lastLabel = null;
                    }
                    FieldModel field = new FieldModel(label, widgetName, type);
                    type.analyseArguments(field, widgetDefinition.getArguments());
                    if (materializer.canAddFieldTo(field, line)) {
                        add(field);
                    } else {
                        reportError(widgetDefinition.getSpan(), "line too long: cannot add widget '" + widgetName + "'");
                    }
                }
            }
        }

        private void add(WidgetModel widget) {
            addToParentGroup(widget);
            line.add(widget);
        }

        private void addToParentGroup(WidgetModel widget) {
            if (groups.size() > 0) {
                groups.peek().add(widget);
            }
        }

        @Override
        public void visitWidgetGroup(WidgetGroup widgetGroup) {
            if (widgetGroup.getName() == null) {
                reportError(widgetGroup.getSpan(), "multiline group not handled");
            } else {
                String groupName = widgetGroup.getName().getName();
                if (screenModel.getWidget(groupName) != null) {
                    reportError(widgetGroup.getName().getSpan(), "widget '" + groupName + "' already defined");
                } else {
                    GroupModel group = new GroupModel(groupName);
                    screenModel.addWidget(group);
                    groups.push(group);
                    for (Widget widget : widgetGroup.getWidgets()) {
                        widget.visit(this);
                    }
                    groups.pop();
                    if (groups.isEmpty()) {
                        // rootGroup
                    } else {
                        addToParentGroup(group);
                    }
                }
            }
        }

        public void finishLine() {
            if (lastLabel != null) {
                add(lastLabel);
            }
        }

    }

    private void analyseWidgetLine(ScreenModel screenModel, WidgetLine widgetLine) {
        WidgetLineModel line = new WidgetLineModel(screenModel);
        screenModel.addLine(line);
        WidgetCreation wc = new WidgetCreation(screenModel, line);
        for (Widget widget : widgetLine.getWidgets()) {
            widget.visit(wc);
        }
        wc.finishLine();
    }

    private void reportError(Span span, String message) {
        reporter.reportError(span.getStart(), span.getEnd(), message);
    }

}
