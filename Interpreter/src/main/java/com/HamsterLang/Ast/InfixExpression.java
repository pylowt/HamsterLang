package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class InfixExpression extends Expression{
    private Token token;
    private Expression left;
    private String operator;
    private Expression right;

    public String getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }
    public Expression getRight() {
        return right;
    }

   public String string() {
        return "(" +
                left.string() +
                " " +
                operator +
                " " +
                right.string() +
                ")";
    }
}
