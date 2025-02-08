package com.HamsterLang.Parser;

import com.HamsterLang.Ast.*;
import com.HamsterLang.Lexer.Lexer;
import com.HamsterLang.Tokens.Token;
import com.HamsterLang.Tokens.TokenTypes.TokenType;

import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    private Token curToken;
    private Token peekToken;
    public ArrayList<String> errors;

    public Parser(Lexer l)
    {
        this.lexer = l;
        this.errors = new ArrayList<>();
        nextToken();
        nextToken();
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
            default -> null;
        };
    }

        private VarStatement parseVarStatement()
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

        private ReturnStatement parseReturnStatement()
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
    }

