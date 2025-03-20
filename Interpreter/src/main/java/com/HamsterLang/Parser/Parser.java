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
    private Map<TokenType, PrefixParseFn> prefixParseFns = new HashMap<>();
    private Map<TokenType, InfixParseFn> infixParseFns = new HashMap<>();
    public ArrayList<Statement> statements = new ArrayList<>();
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

    public Parser(Lexer l)
    {
        this.lexer = l;
        this.errors = new ArrayList<>();
        nextToken();
        nextToken();
        prefixParseFns.put(TokenType.IDENT, parseIdentifierFn);
        prefixParseFns.put(TokenType.INT, parseIntegerLiteralFn);
        prefixParseFns.put(TokenType.BANG, parsePrefixExpressionFn);
        prefixParseFns.put(TokenType.MINUS, parsePrefixExpressionFn);
    }

    PrefixParseFn parseIdentifierFn = () -> new Identifier(curToken, curToken.Literal);
    PrefixParseFn parseIntegerLiteralFn = this::parseIntegerLiteral;

    PrefixParseFn parsePrefixExpressionFn = this::parsePrefixExpression;

    private @Nullable Expression parseIntegerLiteral() {
        var lit = new IntegerLiteral(curToken);
        long parsedValue;
        try {
            parsedValue = Long.parseLong(curToken.Literal);
        } catch (NumberFormatException e) {
            errors.add("Could not parse " + curToken.Literal + " as integer");
            return null;
        }
        lit.setValue(parsedValue);
        return lit;
    }

    private @NotNull Expression parsePrefixExpression(){
       var expression = new PrefixExpression(curToken, curToken.Literal);
       nextToken();
       expression.setRight(parseExpression(Precedents.PREFIX.ordinal()));
       return expression;
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
        var stmnt = new ExpressionStatement(curToken);
        stmnt.setExpression(parseExpression(Precedents.LOWEST.ordinal()));
        if (peekTokenIs(TokenType.SEMICOLON)) {
            nextToken();
        }
        return stmnt;
    }

    private @Nullable Expression parseExpression(int precedence) {
        PrefixParseFn prefix = prefixParseFns.get(curToken.Type);
        if (prefix == null) {
            noPrefixParseFnError(curToken.Type);
            return null;
        }
        Expression leftExp;
        leftExp = prefix.parse();
        return leftExp;
    }

    private @Nullable VarStatement parseVarStatement()
    {
        var stmt = new VarStatement(curToken);

        if (!expectPeek(TokenType.IDENT))
            return null;

        stmt.name = new Identifier(curToken, curToken.Literal);

        if (!expectPeek(TokenType.ASSIGN))
            return null;

        // TODO: Skipping the expressions until encounter a semicolon
        while (!curTokenIs(TokenType.SEMICOLON))
        {
            nextToken();
        }
        return stmt;
    }

        private @NotNull ReturnStatement parseReturnStatement()
        {
            var stmt = new ReturnStatement(curToken);
            // TODO: Skipping the expressions until encounter a semicolon
            while (!curTokenIs(TokenType.SEMICOLON))
            {
                nextToken();
            }
            return stmt;
        }

        private boolean curTokenIs(TokenType t)
        {
            return curToken.Type == t;
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
                return true;
            }
            PeekError(t);
            return false;
        }

        private void PeekError(TokenType t)
        {
            String message = "Expected next token to be " + t + " got " + peekToken.Type + " instead";
            errors.add(message);
        }

        private void RegisterPrefix(TokenType tokenType, PrefixParseFn fn) {
            prefixParseFns.put(tokenType, fn);
        }
        private void RegisterInfix(TokenType tokenType, InfixParseFn fn) {
             infixParseFns.put(tokenType, fn);
        }

        private void noPrefixParseFnError(TokenType t) {
            errors.add("No prefix parse function for " + t.getSymbol());
        }


}

