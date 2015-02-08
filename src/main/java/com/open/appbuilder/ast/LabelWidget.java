package com.open.appbuilder.ast;

public class LabelWidget extends Widget {

    private String content;

    public LabelWidget(Span span, String content) {
    	super(span);
        this.content = content;
    }

	@Override
	public void visit(WidgetVisitor visitor) {
		visitor.visitLabelWidget(this);
	}

	public String getContent() {
		return content;
	}

}
