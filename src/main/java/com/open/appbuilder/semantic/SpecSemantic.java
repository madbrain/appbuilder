package com.open.appbuilder.semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.open.appbuilder.ErrorReporter;
import com.open.appbuilder.Materializer;
import com.open.appbuilder.ast.AssignCommand;
import com.open.appbuilder.ast.BindCommand;
import com.open.appbuilder.ast.Command;
import com.open.appbuilder.ast.CommandVisitor;
import com.open.appbuilder.ast.EntityCommand;
import com.open.appbuilder.ast.EnumCommand;
import com.open.appbuilder.ast.Field;
import com.open.appbuilder.ast.Identifier;
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
import com.open.appbuilder.type.EntityField;
import com.open.appbuilder.type.EntityType;
import com.open.appbuilder.type.EnumElement;
import com.open.appbuilder.type.EnumType;
import com.open.appbuilder.type.IntegerType;
import com.open.appbuilder.type.StringType;
import com.open.appbuilder.type.Type;

public class SpecSemantic {

    private final ErrorReporter reporter;
    private final Materializer materializer;

    public SpecSemantic(ErrorReporter reporter, Materializer materializer) {
        this.reporter = reporter;
        this.materializer = materializer;
    }

    public ApplicationModel analyse(Spec spec) {
        ApplicationModel model = new ApplicationModel();
        model.addType("Integer", new IntegerType());
        model.addType("String", new StringType());
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
            Scope scope = new Scope();
            for (Command command : screen.getCommands()) {
            	command.visit(new CommandCreation(screenModel, scope, model));
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
    
    private class CommandCreation implements CommandVisitor {

		private ApplicationModel model;

		public CommandCreation(ScreenModel screenModel, Scope scope, ApplicationModel model) {
			this.model = model;
		}

		@Override
		public void visitEnum(EnumCommand enumCommand) {
			String enumName = enumCommand.getName().getName();
			if (model.getType(enumName) != null) {
				reportError(enumCommand.getName().getSpan(), "type '" + enumName + "' already defined");
			} else {
				Map<String, EnumElement> elements = new HashMap<>();
				for (Identifier enumElement : enumCommand.getElements()) {
					if (elements.containsKey(enumElement.getName())) {
						reportError(enumElement.getSpan(), "enum '" + enumName
								+ "' already contains element '" + enumElement.getName() + "'");
					} else {
						elements.put(enumElement.getName(), new EnumElement(enumElement.getName()));
					}
				}
				model.addType(enumName, new EnumType(enumName, elements));
			}
		}

		@Override
		public void visitEntity(EntityCommand entityCommand) {
			String entityName = entityCommand.getName().getName();
			if (model.getType(entityName) != null) {
				reportError(entityCommand.getName().getSpan(), "type '" + entityName + "' already defined");
			} else {
				Map<String, EntityField> fields = new HashMap<>();
				model.addType(entityName, new EntityType(entityName, fields));
				for (Field entityField : entityCommand.getFields()) {
					String fieldName = entityField.getName().getName();
					if (fields.containsKey(fieldName)) {
						reportError(entityField.getName().getSpan(), "entity '" + entityName
								+ "' already contains element '" + fieldName + "'");
					} else {
						Type fieldType = model.getType(entityField.getType().getName());
						if (fieldType == null) {
							reportError(entityField.getType().getSpan(), "unknown type");
						} else {
							fields.put(fieldName, new EntityField(fieldName, fieldType));
						}
					}
				}
			}
		}

		@Override
		public void visitAssign(AssignCommand assignCommand) {
			reportError(assignCommand.getSpan(), "not implemented");
		}

		@Override
		public void visitBind(BindCommand bindCommand) {
			reportError(bindCommand.getSpan(), "not implemented");
		}
    }
    
    private void reportError(Span span, String message) {
        reporter.reportError(span.getStart(), span.getEnd(), message);
    }

}
