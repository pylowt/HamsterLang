package com.HamsterLang.Parser;

import com.HamsterLang.Ast.*;
import com.HamsterLang.Lexer.Lexer;
import com.HamsterLang.Tokens.Token;
import com.HamsterLang.Tokens.TokenTypes.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private final Lexer lexer;
    private Token curToken;
    private Token peekToken;
    public ArrayList<String> errors;
    private final Map<TokenType, PrefixParseFn> prefixParseFns = new HashMap<>();
    private final Map<TokenType, InfixParseFn> infixParseFns = new HashMap<>();
    private final HashMap <TokenType, Precedents> precedences = new HashMap<>();
    private enum Precedents {
        _skip,
        LOWEST,
        EQUALS,
        LESSGREATER,
        SUM,
        PRODUCT,
        PREFIX,
        CALL
    }
    private final Tracer tracer = new Tracer();

    public Parser(Lexer l)
    {
        this.lexer = l;
        this.errors = new ArrayList<>();
        nextToken();
        nextToken();
        registerPrefixFunctions();
        registerInfixFunctions();
        intialisePrecedences();
    }

    private void registerPrefixFunctions() {
        prefixParseFns.put(TokenType.IDENT, parseIdentifierFn);
        prefixParseFns.put(TokenType.INT, parseIntegerLiteralFn);
        prefixParseFns.put(TokenType.BANG, parsePrefixExpressionFn);
        prefixParseFns.put(TokenType.MINUS, parsePrefixExpressionFn);
    }
    private void registerInfixFunctions() {
        infixParseFns.put(TokenType.PLUS, this::parseInfixExpression);
        infixParseFns.put(TokenType.MINUS, this::parseInfixExpression);
        infixParseFns.put(TokenType.SLASH, this::parseInfixExpression);
        infixParseFns.put(TokenType.ASTERISK, this::parseInfixExpression);
        infixParseFns.put(TokenType.EQ, this::parseInfixExpression);
        infixParseFns.put(TokenType.NOT_EQ, this::parseInfixExpression);
        infixParseFns.put(TokenType.LT, this::parseInfixExpression);
        infixParseFns.put(TokenType.GT, this::parseInfixExpression);
    }

    private void intialisePrecedences() {
        precedences.put(TokenType.EQ, Precedents.EQUALS);
        precedences.put(TokenType.NOT_EQ, Precedents.EQUALS);
        precedences.put(TokenType.LT, Precedents.LESSGREATER);
        precedences.put(TokenType.GT, Precedents.LESSGREATER);
        precedences.put(TokenType.PLUS, Precedents.SUM);
        precedences.put(TokenType.MINUS, Precedents.SUM);
        precedences.put(TokenType.SLASH, Precedents.PRODUCT);
        precedences.put(TokenType.ASTERISK, Precedents.PRODUCT);
    }

    PrefixParseFn parseIdentifierFn = () -> new Identifier(curToken, curToken.Literal);
    PrefixParseFn parseIntegerLiteralFn = this::parseIntegerLiteral;
    PrefixParseFn parsePrefixExpressionFn = this::parsePrefixExpression;

    private @Nullable Expression parseIntegerLiteral() {
        tracer.trace("parseIntegerLiteral");
        try {
            var lit = new IntegerLiteral(curToken);
            try {
                lit.setValue(Long.parseLong(curToken.Literal));
            } catch (NumberFormatException e) {
                errors.add("Could not parse " + curToken.Literal + " as integer");
                return null;
            }
            return lit;
        } finally {
            tracer.untrace("parseIntegerLiteral");
        }
    }

    private @NotNull Expression parsePrefixExpression(){
        tracer.trace("parsePrefixExpression");
        try {
            var expression = new PrefixExpression(curToken, curToken.Literal);
            nextToken();
            expression.setRight(parseExpression(Precedents.PREFIX.ordinal()));
            return expression;
        } finally {
            tracer.untrace("parsePrefixExpression");
        }
    }

    private @NotNull Expression parseInfixExpression(Expression left) {
        tracer.trace("parseInfixExpression");
        try {
            var expression = new InfixExpression(curToken, curToken.Literal, left);
            var precedence = curPrecedence();
            nextToken();
            expression.setRight(parseExpression(precedence));
            return expression;
        } finally {
            tracer.untrace("parseInfixExpression");
        }
    }

    private void nextToken()
    {
        curToken = peekToken;
        peekToken = lexer.nextToken();
    }

    public ASTRoot parseProgram()
    {
        var program = new ASTRoot();

        while (curToken.Type != TokenType.EOF)
        {
            var stmt = parseStatement();
            if (stmt != null)
            {
                program.statements.add(stmt);
                nextToken();
            }
        }
        return program;
    }

    private Statement parseStatement()
    {
        return switch (curToken.Type) {
            case VAR -> parseVarStatement();
            case RETURN -> parseReturnStatement();
            default -> parseExpressionStatement();
        };
    }

    private @NotNull ExpressionStatement parseExpressionStatement() {
//        TODO create a method that takes a Callable or Runnable and handles the tracing logic
        tracer.trace("parseExpressionStatement");
        try {
            var stmnt = new ExpressionStatement(curToken);
            var exp = parseExpression(Precedents.LOWEST.ordinal());
            stmnt.setExpression(exp);
            if (peekTokenIs(TokenType.SEMICOLON)) {
                nextToken();
            }
            return stmnt;
        } finally {
            tracer.untrace("parseExpressionStatement");
        }
    }

    private @Nullable Expression parseExpression(int precedence) {
        tracer.trace("parseExpression");
        try {
            PrefixParseFn prefix = prefixParseFns.get(curToken.Type);
            if (prefix == null) {
                noPrefixParseFnError(curToken.Type);
                return null;
            }
            Expression leftExp;
            leftExp = prefix.parse();
            while (!peekTokenIs(TokenType.SEMICOLON) && precedence < peekPrecedence()) {
                InfixParseFn infix = infixParseFns.get(peekToken.Type);
                if (infix == null) {
                    return leftExp;
                }
                nextToken();
                leftExp = infix.parse(leftExp);
            }
            return leftExp;
        } finally {
            tracer.untrace("parseExpression");
        }
    }

    private @Nullable VarStatement parseVarStatement()
    {
        var stmt = new VarStatement(curToken);

        if (expectPeek(TokenType.IDENT))
            return null;

        stmt.name = new Identifier(curToken, curToken.Literal);

        if (expectPeek(TokenType.ASSIGN))
            return null;

        // TODO: Skipping the expressions until encounter a semicolon
        while (curTokenIs())
        {
            nextToken();
        }
        return stmt;
    }

    private @NotNull ReturnStatement parseReturnStatement()
    {
            var stmt = new ReturnStatement(curToken);
            // TODO: Skipping the expressions until encounter a semicolon
            while (curTokenIs())
            {
                nextToken();
            }
            return stmt;
        }

    private boolean curTokenIs()
    {
        return curToken.Type != TokenType.SEMICOLON;
    }

    private boolean peekTokenIs(TokenType t)
    {
        return peekToken.Type == t;
    }

    private boolean expectPeek(TokenType t)
    {
        if (peekTokenIs(t))
        {
            nextToken();
            return false;
        }
        PeekError(t);
        return true;
    }

    private void PeekError(TokenType t)
    {
        String message = "Expected next token to be " + t + " got " + peekToken.Type + " instead";
        errors.add(message);
    }

    private void noPrefixParseFnError(TokenType t) {
        errors.add("No prefix parse function for " + t.getSymbol() + " found.");
    }

    private int peekPrecedence() {
        if (precedences.containsKey(peekToken.Type)) {
            return precedences.get(peekToken.Type).ordinal();
        }
        return Precedents.LOWEST.ordinal();
    }

    private int curPrecedence() {
        if (precedences.containsKey(curToken.Type)) {
            return precedences.get(curToken.Type).ordinal();
        }
        return Precedents.LOWEST.ordinal();
    }

}
