package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class PrefixExpression extends Expression {
    Token token;
    private String operator;
    private Expression right;

    public void expressionNode() {}
    public String tokenLiteral() {
        return token.Literal;
    }
    public String string() {
        return "(" +
                operator +
                right.string() +
                ")";
    }

    public String getOperator() {
        return this.operator;
    }

    public Expression getRight() {
        return this.right;
    }
}
