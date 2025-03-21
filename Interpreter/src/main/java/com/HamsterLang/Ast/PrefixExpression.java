package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class PrefixExpression extends Expression {
    private final Token token;
    private final String operator;
    private Expression right;

    public PrefixExpression(Token curToken, String operator) {
        this.token = curToken;
        this.operator = operator;
    }

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

    public void setRight(Expression exp) {
        this.right = exp;
    }
}
