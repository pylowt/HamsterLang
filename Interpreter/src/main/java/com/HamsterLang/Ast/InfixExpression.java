package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class InfixExpression extends Expression{
    private final Token token;
    private final Expression left;
    private final String operator;
    private Expression right;

    public InfixExpression(Token token, String literal, Expression left) {
        this.token = token;
        this.operator = literal;
        this.left = left;

    }

    public String getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }
    public Expression getRight() {
        return right;
    }

    @Override
    public String string() {
         return "(" +
                left.string() +
                " " +
                operator +
                " " +
                right.string() +
                ")";
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public Token getToken() {
        return token;
    }
}
