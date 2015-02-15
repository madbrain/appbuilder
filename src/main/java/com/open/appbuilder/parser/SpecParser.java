package com.open.appbuilder.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.open.appbuilder.ErrorReporter;
import com.open.appbuilder.ast.AssignCommand;
import com.open.appbuilder.ast.AstList;
import com.open.appbuilder.ast.BindCommand;
import com.open.appbuilder.ast.Command;
import com.open.appbuilder.ast.EntityCommand;
import com.open.appbuilder.ast.EnumCommand;
import com.open.appbuilder.ast.EqualsExpression;
import com.open.appbuilder.ast.Expression;
import com.open.appbuilder.ast.Field;
import com.open.appbuilder.ast.FieldExpression;
import com.open.appbuilder.ast.Identifier;
import com.open.appbuilder.ast.LabelWidget;
import com.open.appbuilder.ast.ObjectExpression;
import com.open.appbuilder.ast.Screen;
import com.open.appbuilder.ast.Span;
import com.open.appbuilder.ast.Spec;
import com.open.appbuilder.ast.StringExpr;
import com.open.appbuilder.ast.VarExpression;
import com.open.appbuilder.ast.Widget;
import com.open.appbuilder.ast.WidgetDefinition;
import com.open.appbuilder.ast.WidgetGroup;
import com.open.appbuilder.ast.WidgetLine;

public class SpecParser {

    private enum TokenType {

        AT("@"),
        ASSIGN("="),
        BIND("<->"),
        DOT("."),
        ENUM("enum"),
        ENTITY("entity"),
        EQUALS("=="),
        IDENT,
        STAR("*"),
        NEWLINE,
        STRING,
        COLON(":"),
        LPAR("("),
        RPAR(")"),
        ENDG(")-"),
        COMA(","),

        EOF;
        
        private String display;
	
        private TokenType() {
        	
        }
        
        private TokenType(String display) {
        	this.display = display;
        }
        
        public String getDisplay() {
			if (display != null) {
				return '\'' + display + '\'';
			}
			return "<" + name() +">";
		}

    }

    private static final Map<String, TokenType> KEYWORDS = new HashMap<String, TokenType>();

    static {
        KEYWORDS.put("enum", TokenType.ENUM);
        KEYWORDS.put("entity", TokenType.ENTITY);
    }

    private static class Token {

        private final String value;
        private final TokenType type;
		private final Span span;

        public Token(TokenType type, Span span, String value) {
            this.type = type;
            this.span = span;
            this.value = value;
        }

        public Token(TokenType type, Span span) {
            this(type, span, null);
        }
        
        public String getDisplay() {
        	if (type == TokenType.STRING) {
        		return '"' + value + '"';
        	}
			if (value != null) {
				return '\'' + value + '\'';
			}
			return type.getDisplay();
		}

        @Override
        public String toString() {
            return type.name() + (value != null ? "(" + value + ")" : "");
        }

    }

    private final ErrorReporter reporter;
    private Reader reader;
    private Token token;
    private int tokenStart;
    private int position;
    private int lastChar;
    
    public SpecParser(ErrorReporter reporter) {
    	this.reporter = reporter;
    }

    public Spec parse(Reader reader) throws ParseException {
        reset(reader);
        return parseSpec();
    }
    
    private Spec parseSpec() throws ParseException {
    	Spec spec = new Spec();
        while (! testNext(TokenType.EOF)) {
        	spec.getScreens().add(parseScreen(spec));
        }
        expect(TokenType.EOF);
        return spec;
	}

	private void reset(Reader reader) throws ParseException {
    	this.reader = reader;
    	this.position = 0;
    	this.lastChar = -1;
    	scanToken();
    }

    private Screen parseScreen(Spec spec) throws ParseException {
    	skipNewlines();
        expect(TokenType.STAR);
        Identifier identifier = expectIdentifier();
        Screen screen = new Screen(identifier);
        if (skipIfNext(TokenType.NEWLINE)) {
	        while (!skipIfNext(TokenType.NEWLINE)) {
	            screen.getWidgetLines().add(parseWidgetsLine());
	            if (testNext(TokenType.EOF)) {
	                break;
	            }
	        }
	        skipNewlines();
	        while (!testNext(TokenType.STAR, TokenType.EOF)) {
	            screen.getCommands().add(parseCommandLine());
	            skipIfNext(TokenType.NEWLINE);
	        }
        }
        return screen;
    }
    
    private void skipNewlines() throws ParseException {
    	while (skipIfNext(TokenType.NEWLINE)) {
        	;
        }
    }

    private WidgetLine parseWidgetsLine() throws ParseException {
        WidgetLine line = new WidgetLine(parseWidgets(TokenType.NEWLINE, TokenType.EOF));
        skipIfNext(TokenType.NEWLINE);
        return line;
    }

    private AstList<Widget> parseWidgets(TokenType... delimiters) throws ParseException {
    	Span lineSpan = new Span(token.span.getStart(), token.span.getStart());
        List<Widget> line = new ArrayList<>();
        while (!testNext(delimiters)) {
        	Span span = new Span(token.span.getStart(), token.span.getStart());
            if (testNext(TokenType.STRING)) {
                line.add(new LabelWidget(token.span, token.value));
                scanToken();
            } else if (skipIfNext(TokenType.AT)) {
                Identifier widgetType = expectIdentifier();
                expect(TokenType.LPAR);
                Identifier widgetName = expectIdentifier();
                List<StringExpr> arguments = Collections.emptyList();
                if (skipIfNext(TokenType.COMA)) {
                    arguments = parseArguments();
                }
                span = span.add(expect(TokenType.RPAR));
                line.add(new WidgetDefinition(span, widgetType, widgetName, arguments));
            } else if (skipIfNext(TokenType.LPAR)) {
                List<Widget> widgets = parseWidgets(TokenType.RPAR, TokenType.ENDG);
                Identifier groupName = null;
                if (skipIfNext(TokenType.ENDG)) {
                    groupName = expectIdentifier();
                    span = span.add(groupName.getSpan());
                } else {
                	span = span.add(expect(TokenType.RPAR));
                }
                line.add(new WidgetGroup(span, groupName, widgets));
            } else {
                throw unexpected();
            }
            lineSpan = lineSpan.add(span);
        }
        return new AstList<Widget>(lineSpan, line);
    }

    private List<StringExpr> parseArguments() throws ParseException {
        List<StringExpr> arguments = new ArrayList<>();
        while (!testNext(TokenType.RPAR)) {
            if (testNext(TokenType.STRING)) {
                arguments.add(new StringExpr(token.value));
                scanToken();
            } else {
                throw unexpected();
            }
            skipIfNext(TokenType.COMA);
        }
        return arguments;
    }

    private Command parseCommandLine() throws ParseException {
    	Span span = token.span;
        if (skipIfNext(TokenType.ENUM)) {
            Identifier name = expectIdentifier();
            expect(TokenType.LPAR);
            List<Identifier> elements = parseIdentifierList();
            span = span.add(expect(TokenType.RPAR));
            return new EnumCommand(span, name, elements);
        } else if (skipIfNext(TokenType.ENTITY)) {
            Identifier name = expectIdentifier();
            expect(TokenType.LPAR);
            List<Field> fields = parseFields();
            span = span.add(expect(TokenType.RPAR));
            return new EntityCommand(span, name, fields);
        } else {
            Expression left = parseExpression();
            if (skipIfNext(TokenType.ASSIGN)) {
            	Expression right = parseExpression();
            	span = span.add(right.getSpan());
                return new AssignCommand(span, left, right);
            } else if (skipIfNext(TokenType.BIND)) {
            	Expression right = parseExpression();
            	span = span.add(right.getSpan());
                return new BindCommand(span, left, right);
            }
            throw unexpected();
        }
    }

    private List<Identifier> parseIdentifierList() throws ParseException {
        List<Identifier> elements = new ArrayList<>();
        while (true) {
            elements.add(expectIdentifier());
            if (!skipIfNext(TokenType.COMA)) {
                break;
            }
        }
        return elements;
    }

    private List<Field> parseFields() throws ParseException {
        List<Field> fields = new ArrayList<>();
        while (!testNext(TokenType.RPAR)) {
            Identifier name = expectIdentifier();
            expect(TokenType.COLON);
            Identifier type = expectIdentifier();
            fields.add(new Field(name, type));
            if (!skipIfNext(TokenType.COMA)) {
                break;
            }
        }
        return fields;
    }

    private Expression parseExpression() throws ParseException {
        return parseRelationalExpression();
    }

    private Expression parseRelationalExpression() throws ParseException {
        Expression expr = parseUnary();
        if (skipIfNext(TokenType.EQUALS)) {
            Expression right = parseUnary();
            return new EqualsExpression(expr, right);
        }
        return expr;
    }

    private Expression parseUnary() throws ParseException {
        Expression expr = parsePrimary();
        while (skipIfNext(TokenType.DOT)) {
        	Identifier ident = expectIdentifier();
            expr = new FieldExpression(expr.getSpan().add(ident.getSpan()), expr, ident);
        }
        return expr;
    }

    private Expression parsePrimary() throws ParseException {
    	Span span = token.span;
        if (testNext(TokenType.IDENT)) {
            Identifier name = expectIdentifier();
            return new VarExpression(name);
        }
        if (skipIfNext(TokenType.AT)) {
            Identifier name = expectIdentifier();
            expect(TokenType.LPAR);
            List<Expression> arguments = parseExpressionList(TokenType.RPAR);
            span = span.add(expect(TokenType.RPAR));
            return new ObjectExpression(span, name, arguments);
        }
        throw unexpected();
    }

    private List<Expression> parseExpressionList(TokenType delim) throws ParseException {
        List<Expression> expressions = new ArrayList<>();
        while (!testNext(delim)) {
            expressions.add(parseExpression());
            if (!skipIfNext(TokenType.COMA)) {
                break;
            }
        }
        return expressions;
    }
    
    private Span expect(TokenType type) throws ParseException {
        if (token.type != type) {
        	String message = "expecting " + type.getDisplay();
        	reporter.reportError(token.span.getStart(), token.span.getEnd(), message);
            throw new ParseException(message);
        }
        Span span = token.span;
        scanToken();
        return span;
    }
    
    private ParseException unexpected(Span span, String message) {
    	reporter.reportError(span.getStart(), span.getEnd(), message);
        return new ParseException(message);
	}

    private ParseException unexpected() {
    	return unexpected(token.span, "unexpected token " + token.getDisplay());
    }

    private Identifier expectIdentifier() throws ParseException {
        if (token.type != TokenType.IDENT) {
            unexpected(token.span, "expecting <IDENT>");
        }
        Identifier identifier = new Identifier(token.span, token.value);
        scanToken();
        return identifier;
    }
    
    private boolean testNext(TokenType... types) {
        for (TokenType t : types) {
            if (token.type == t) {
                return true;
            }
        }
        return false;
    }

	private boolean skipIfNext(TokenType type) throws ParseException {
        if (token.type == type) {
            scanToken();
            return true;
        }
        return false;
    }

    private Token scanToken() throws ParseException {
        while (true) {
        	tokenStart = position;
            int c = getChar();
            if (c < 0) {
                return token(TokenType.EOF);
            }
            if (isSpace(c)) {
                continue;
            }
            if (c == '\n') {
                return token(TokenType.NEWLINE);
            }
            if (isLetter(c)) {
                return scanIdentifier(c);
            }
            if (c == '"') {
                return token(scanString());
            }
            if (c == '*') {
                return token(TokenType.STAR);
            }
            if (c == ':') {
                return token(TokenType.COLON);
            }
            if (c == '.') {
                return token(TokenType.DOT);
            }
            if (c == '@') {
                return token(TokenType.AT);
            }
            if (c == '(') {
                return token(TokenType.LPAR);
            }
            if (c == ',') {
                return token(TokenType.COMA);
            }
            if (c == '=') {
                c = getChar();
                if (c == '=') {
                    return token(TokenType.EQUALS);
                }
                ungetChar(c);
                return token(TokenType.ASSIGN);
            }
            if (c == '<') {
                c = getChar();
                if (c == '-') {
                    c = getChar();
                    if (c == '>') {
                        return token(TokenType.BIND);
                    }
                }
                throw unexpected(new Span(tokenStart, position),
                		"unexpected char '" + ((char) c) + "'");
            }
            if (c == ')') {
                c = getChar();
                if (c == '-') {
                    return token(TokenType.ENDG);
                }
                ungetChar(c);
                return token(TokenType.RPAR);
            }
            throw unexpected(new Span(tokenStart, position), 
            		"unexpected char '" + ((char) c) + "'");
        }
    }

	private Token scanString() throws ParseException {
        StringBuilder builder = new StringBuilder();
        int c;
        while ((c = getChar()) != '"') {
            builder.append((char) c);
        }
        return token(TokenType.STRING, builder.toString());
    }

    private Token scanIdentifier(int c) throws ParseException {
        StringBuilder builder = new StringBuilder();
        do {
            builder.append((char) c);
            c = getChar();
        } while (isLetterOrDigit(c));
        ungetChar(c);
        String ident = builder.toString();
        TokenType type = KEYWORDS.get(ident);
        if (type != null) {
            return token(type);
        }
        return token(TokenType.IDENT, ident);
    }

    private Token token(Token token) {
        return this.token = token;
    }
    
    private Token token(TokenType type) {
    	return token(new Token(type, span()));
    }
    
    private Token token(TokenType type, String value) {
    	return token(new Token(type, span(), value));
    }

    private Span span() {
    	return new Span(tokenStart, position);
    }

    private static boolean isLetterOrDigit(int c) {
        return isLetter(c) || isDigit(c);
    }

    private static boolean isDigit(int c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLetter(int c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    private static boolean isSpace(int c) {
        return c == ' ' || c == '\t' || c == '\r';
    }

    private int getChar() throws ParseException {
        if (lastChar >= 0) {
            int t = lastChar;
            lastChar = -1;
            ++position;
            return t;
        }
        try {
        	return reader.read();
        } catch (IOException e) {
        	throw new ParseException(e);
        } finally {
        	++position;
        }
    }

    private void ungetChar(int c) {
    	--position;
        this.lastChar = c;
    }

}
