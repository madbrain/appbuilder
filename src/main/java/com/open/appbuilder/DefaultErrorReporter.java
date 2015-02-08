package com.open.appbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class DefaultErrorReporter implements ErrorReporter {
	
	private static class Error {

		private int start;
		private int end;
		private String message;

		public Error(int start, int end, String message) {
			this.start = start;
			this.end = end;
			this.message = message;
		}

		public boolean isIn(int start, int end) {
			return this.start >= start && this.start < end;
		}
		
	}
	
	private List<Error> errors = new ArrayList<>();

	@Override
	public void reportError(int start, int end, String message) {
		errors.add(new Error(start, end, message));
	}

	public void buildErrorReport(Reader input, PrintWriter out) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		String line;
		int position = 0;
		int lineNumber = 1;
		while ((line = reader.readLine()) != null) {
			position = buildErrorReportInLine(line, position, lineNumber, out);
			++lineNumber;
		}
	}

	private int buildErrorReportInLine(String line, int position, int lineNumber, PrintWriter out) {
		int end = position + line.length() + 1;
		for (Error error : errors) {
			if (error.isIn(position, end)) {
				out.println(line);
				int i = 0;
				while (true) {
					if (i < (error.start - position)) { 
						out.print(' ');
					} else if (i < Math.min((error.end - position), end)) {
						out.print('^');
					} else {
						break;
					}
					++i;
				}
				out.println();
				out.println("[" + lineNumber + "] " + error.message);
			}
		}
		return end;
	}

	public boolean hasError() {
		return errors.size() > 0;
	}

}
