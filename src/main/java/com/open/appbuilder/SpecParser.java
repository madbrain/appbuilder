package com.open.appbuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecParser {

    private enum TokenType {

        AT,
        ASSIGN,
        BIND,
        DOT,
        ENUM,
        ENTITY,
        EQUALS,
        IDENT,
        STAR,
        NEWLINE,
        STRING,
        COLON,
        LPAR,
        RPAR,
        ENDG,
        COMA,

        EOF;

    }

    private static final Map<String, TokenType> KEYWORDS = new HashMap<String, TokenType>();

    static {
        KEYWORDS.put("enum", TokenType.ENUM);
        KEYWORDS.put("entity", TokenType.ENTITY);
    }

    private static class Token {

        private final String value;
        private final TokenType type;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Token(TokenType type) {
            this(type, null);
        }

        @Override
        public String toString() {
            return type.name() + (value != null ? "(" + value + ")" : "");
        }

    }

    private Reader reader;
    private Token token;
    private int lastChar = -1;

    public Spec parse(Reader reader) throws IOException {
        this.reader = reader;
        scanToken();
        Spec spec = new Spec();
        spec.getScreens().add(parseScreen(spec));
        return spec;
    }

    private Screen parseScreen(Spec spec) throws IOException {
        expect(TokenType.STAR);
        Identifier identifier = expectIdentifier();
        Screen screen = new Screen(identifier);
        expect(TokenType.NEWLINE);
        while (!skipIfNext(TokenType.NEWLINE)) {
            screen.getWidgetLines().add(parseWidgetsLine());
            if (testNext(TokenType.EOF)) {
                break;
            }
        }
        while (skipIfNext(TokenType.NEWLINE)) {

        }
        while (!testNext(TokenType.STAR, TokenType.EOF)) {
            screen.getCommands().add(parseCommandLine());
            skipIfNext(TokenType.NEWLINE);
        }
        return screen;
    }

    private WidgetLine parseWidgetsLine() throws IOException {
        WidgetLine line = new WidgetLine(parseWidgets(TokenType.NEWLINE, TokenType.EOF));
        skipIfNext(TokenType.NEWLINE);
        return line;
    }

    private List<Widget> parseWidgets(TokenType... delimiters) throws IOException {
        List<Widget> line = new ArrayList<>();
        while (!testNext(delimiters)) {
            if (testNext(TokenType.STRING)) {
                line.add(new LabelWidget(token.value));
                scanToken();
            } else if (skipIfNext(TokenType.AT)) {
                Identifier widgetType = expectIdentifier();
                expect(TokenType.LPAR);
                Identifier widgetName = expectIdentifier();
                List<StringExpr> arguments = Collections.emptyList();
                if (skipIfNext(TokenType.COMA)) {
                    arguments = parseArguments();
                }
                expect(TokenType.RPAR);
                line.add(new WidgetDefinition(widgetType, widgetName, arguments));
            } else if (skipIfNext(TokenType.LPAR)) {
                List<Widget> widgets = parseWidgets(TokenType.RPAR, TokenType.ENDG);
                Identifier groupName = null;
                if (skipIfNext(TokenType.ENDG)) {
                    groupName = expectIdentifier();
                } else {
                    expect(TokenType.RPAR);
                }
                line.add(new WidgetGroup(groupName, widgets));
            } else {
                throw unexpected();
            }
        }
        return line;
    }

    private List<StringExpr> parseArguments() throws IOException {
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

    private Command parseCommandLine() throws IOException {
        if (skipIfNext(TokenType.ENUM)) {
            Identifier name = expectIdentifier();
            expect(TokenType.LPAR);
            List<Identifier> elements = parseIdentifierList();
            expect(TokenType.RPAR);
            return new EnumCommand(name, elements);
        } else if (skipIfNext(TokenType.ENTITY)) {
            Identifier name = expectIdentifier();
            expect(TokenType.LPAR);
            List<Field> fields = parseFields();
            expect(TokenType.RPAR);
            return new EntityCommand(name, fields);
        } else {
            Expression left = parseExpression();
            if (skipIfNext(TokenType.ASSIGN)) {
                return new AssignCommand(left, parseExpression());
            } else if (skipIfNext(TokenType.BIND)) {
                return new BindCommand(left, parseExpression());
            }
            throw unexpected();
        }
    }

    private List<Identifier> parseIdentifierList() throws IOException {
        List<Identifier> elements = new ArrayList<>();
        while (true) {
            elements.add(expectIdentifier());
            if (!skipIfNext(TokenType.COMA)) {
                break;
            }
        }
        return elements;
    }

    private List<Field> parseFields() throws IOException {
        List<Field> fields = new ArrayList<>();
        while (true) {
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

    private Expression parseExpression() throws IOException {
        return parseRelationalExpression();
    }

    private Expression parseRelationalExpression() throws IOException {
        Expression expr = parseUnary();
        if (skipIfNext(TokenType.EQUALS)) {
            Expression right = parseUnary();
            return new EqualsExpression(expr, right);
        }
        return expr;
    }

    private Expression parseUnary() throws IOException {
        Expression expr = parsePrimary();
        while (skipIfNext(TokenType.DOT)) {
            expr = new FieldExpression(expr, expectIdentifier());
        }
        return expr;
    }

    private Expression parsePrimary() throws IOException {
        if (testNext(TokenType.IDENT)) {
            Identifier name = expectIdentifier();
            return new VarExpression(name);
        }
        if (skipIfNext(TokenType.AT)) {
            Identifier name = expectIdentifier();
            expect(TokenType.LPAR);
            List<Expression> arguments = parseExpressionList(TokenType.RPAR);
            expect(TokenType.RPAR);
            return new ObjectExpression(name, arguments);
        }
        throw unexpected();
    }

    private List<Expression> parseExpressionList(TokenType delim) throws IOException {
        List<Expression> expressions = new ArrayList<>();
        while (!testNext(delim)) {
            expressions.add(parseExpression());
            if (!skipIfNext(TokenType.COMA)) {
                break;
            }
        }
        return expressions;
    }

    private Token scanToken() throws IOException {
        while (true) {
            int c = getChar();
            if (c < 0) {
                return token(new Token(TokenType.EOF));
            }
            if (isSpace(c)) {
                continue;
            }
            if (c == '\n') {
                return token(new Token(TokenType.NEWLINE));
            }
            if (isLetter(c)) {
                return scanIdentifier(c);
            }
            if (c == '"') {
                return token(scanString());
            }
            if (c == '*') {
                return token(new Token(TokenType.STAR));
            }
            if (c == ':') {
                return token(new Token(TokenType.COLON));
            }
            if (c == '.') {
                return token(new Token(TokenType.DOT));
            }
            if (c == '@') {
                return token(new Token(TokenType.AT));
            }
            if (c == '(') {
                return token(new Token(TokenType.LPAR));
            }
            if (c == ',') {
                return token(new Token(TokenType.COMA));
            }
            if (c == '=') {
                c = getChar();
                if (c == '=') {
                    return token(new Token(TokenType.EQUALS));
                }
                ungetChar(c);
                return token(new Token(TokenType.ASSIGN));
            }
            if (c == '<') {
                c = getChar();
                if (c == '-') {
                    c = getChar();
                    if (c == '>') {
                        return token(new Token(TokenType.BIND));
                    }
                }
                throw new RuntimeException("unexpected char '" + ((char) c) + "'");
            }
            if (c == ')') {
                c = getChar();
                if (c == '-') {
                    return token(new Token(TokenType.ENDG));
                }
                ungetChar(c);
                return token(new Token(TokenType.RPAR));
            }
            throw new RuntimeException("unexpected char '" + ((char) c) + "'");
        }
    }

    private Token scanString() throws IOException {
        StringBuilder builder = new StringBuilder();
        int c;
        while ((c = getChar()) != '"') {
            builder.append((char) c);
        }
        return token(new Token(TokenType.STRING, builder.toString()));
    }

    private Token scanIdentifier(int c) throws IOException {
        StringBuilder builder = new StringBuilder();
        do {
            builder.append((char) c);
            c = getChar();
        } while (isLetterOrDigit(c));
        ungetChar(c);
        String ident = builder.toString();
        TokenType type = KEYWORDS.get(ident);
        if (type != null) {
            return token(new Token(type));
        }
        return token(new Token(TokenType.IDENT, ident));
    }

    private Token token(Token token) {
        return this.token = token;
    }

    private boolean testNext(TokenType... types) {
        for (TokenType t : types) {
            if (token.type == t) {
                return true;
            }
        }
        return false;
    }

    private void expect(TokenType type) throws IOException {
        if (token.type != type) {
            throw new RuntimeException("expecting " + type.name());
        }
        scanToken();
    }

    private RuntimeException unexpected() {
        return new RuntimeException("unexpected token " + token);
    }

    private Identifier expectIdentifier() throws IOException {
        if (token.type != TokenType.IDENT) {
            throw new RuntimeException("expecting IDENT");
        }
        String name = token.value;
        scanToken();
        return new Identifier(name);
    }

    private boolean skipIfNext(TokenType type) throws IOException {
        if (token.type == type) {
            scanToken();
            return true;
        }
        return false;
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

    private int getChar() throws IOException {
        if (lastChar >= 0) {
            int t = lastChar;
            lastChar = -1;
            return t;
        }
        return reader.read();
    }

    private void ungetChar(int c) {
        this.lastChar = c;
    }

}
