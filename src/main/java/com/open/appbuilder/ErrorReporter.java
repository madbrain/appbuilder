package com.open.appbuilder;

public interface ErrorReporter {

	void reportError(int start, int end, String message);

}
