package com.open.appbuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import com.open.appbuilder.ast.Spec;
import com.open.appbuilder.bootstrap.BootstrapViewMaterializer;
import com.open.appbuilder.model.ApplicationModel;
import com.open.appbuilder.parser.ParseException;
import com.open.appbuilder.parser.SpecParser;
import com.open.appbuilder.semantic.SpecSemantic;

public class Launcher {
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		final String filename = "src/test/resources/app2.spec";
		
		BootstrapViewMaterializer materializer = new BootstrapViewMaterializer();
		DefaultErrorReporter reporter = new DefaultErrorReporter();
		boolean hasError = false;
		try {
			SpecParser parser = new SpecParser(new DefaultErrorReporter());
			Spec spec = parser.parse(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			SpecSemantic semantic = new SpecSemantic(reporter, materializer);
			ApplicationModel model = semantic.analyse(spec);
			materializer.generate(model);
		} catch (ParseException e) {
			hasError = true;
		} catch (Exception e) {
			hasError = true;
			e.printStackTrace();
		}
		if (hasError || reporter.hasError()) {
			StringWriter out = new StringWriter();
			reporter.buildErrorReport(new InputStreamReader(new FileInputStream(filename), "UTF-8"), new PrintWriter(out));
			System.out.println(out.toString());
		}
	}
}
