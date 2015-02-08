package com.open.appbuilder.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.open.appbuilder.DefaultErrorReporter;
import com.open.appbuilder.Materializer;
import com.open.appbuilder.ast.Spec;
import com.open.appbuilder.model.FieldModel;
import com.open.appbuilder.model.WidgetLineModel;
import com.open.appbuilder.model.WidgetTypeModel;
import com.open.appbuilder.parser.ParseException;
import com.open.appbuilder.parser.SpecParser;
import com.open.appbuilder.semantic.SpecSemantic;

public class SpecSemanticTest {

	@Test
	public void testDuplicateScreenName() throws IOException {
		String content = "*Screen1\n\n*Screen1";
		String errorMessage = "*Screen1\n ^^^^^^^\n[3] screen 'Screen1' already defined\n";
		Materializer materializer = Mockito.mock(Materializer.class);
		testSpecError(content, errorMessage, materializer, false);
	}
	
	@Test
	public void testUnknownWidgetType() throws IOException {
		String content = "*Screen1\n@Unknown(b)";
		String errorMessage =
				  "@Unknown(b)\n"
				+ " ^^^^^^^\n"
				+ "[2] widget type 'Unknown' unknown\n";
		Materializer materializer = Mockito.mock(Materializer.class);
		testSpecError(content, errorMessage, materializer, false);
	}
	
	@Test
	public void testDuplicateWidgetName() throws IOException {
		String content = "*Screen1\n@Button(b)@Button(b)";
		String errorMessage =
				  "@Button(b)@Button(b)\n"
				+ "                  ^\n"
				+ "[2] widget 'b' already defined\n";
		
		Materializer materializer = Mockito.mock(Materializer.class);
		final WidgetTypeModel BUTTON = new WidgetTypeModel();
		Mockito.when(materializer.getWidgetType("Button")).thenReturn(BUTTON);
		Mockito.when(materializer.canAddFieldTo(Mockito.any(FieldModel.class), Mockito.any(WidgetLineModel.class))).thenReturn(true);
		
		testSpecError(content, errorMessage, materializer, false);
	}
	
	@Test
	public void testLineTooLong() throws IOException {
		String content = "*Screen1\n@Button(b)";
		String errorMessage =
				  "@Button(b)\n"
				+ "^^^^^^^^^^\n"
				+ "[2] line too long: cannot add widget 'b'\n";
		
		Materializer materializer = Mockito.mock(Materializer.class);
		final WidgetTypeModel BUTTON = new WidgetTypeModel();
		Mockito.when(materializer.getWidgetType("Button")).thenReturn(BUTTON);
		Mockito.when(materializer.canAddFieldTo(Mockito.any(FieldModel.class), Mockito.any(WidgetLineModel.class))).thenReturn(false);
		
		testSpecError(content, errorMessage, materializer, false);
	}
	
	@Test
	public void testGroupIsAWidget() throws IOException {
		String content = "*Screen1\n@Button(b) ( \"X\" \"Y\" )-b";
		String errorMessage =
				  "@Button(b) ( \"X\" \"Y\" )-b\n"
				+ "                       ^\n"
				+ "[2] widget 'b' already defined\n";
		
		Materializer materializer = Mockito.mock(Materializer.class);
		final WidgetTypeModel BUTTON = new WidgetTypeModel();
		Mockito.when(materializer.getWidgetType("Button")).thenReturn(BUTTON);
		Mockito.when(materializer.canAddFieldTo(Mockito.any(FieldModel.class), Mockito.any(WidgetLineModel.class))).thenReturn(true);
		
		testSpecError(content, errorMessage, materializer, false);
	}
	
	private void testSpecError(String content, String errorMessage, Materializer materializer, boolean debug) throws IOException {
		DefaultErrorReporter reporter = new DefaultErrorReporter();
		boolean hasError = false;
		try {
			SpecParser parser = new SpecParser(new DefaultErrorReporter());
			Spec spec = parser.parse(new StringReader(content));
			SpecSemantic semantic = new SpecSemantic(reporter, materializer);
			semantic.analyse(spec);
		} catch (ParseException e) {
			hasError = true;
		}
		if (hasError || reporter.hasError()) {
			StringWriter out = new StringWriter();
			reporter.buildErrorReport(new StringReader(content), new PrintWriter(out));
			if (debug) {
				System.out.println(out.toString());
			}
			Assert.assertEquals(errorMessage, out.toString());
		} else {
			Assert.fail("should contains error");
		}
	}
}
