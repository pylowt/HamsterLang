package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class IntegerLiteral extends Expression {
    private Token token;
    private Long value;


    public void setValue(Long value) {
        this.value = value;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    // Constructor for the IntegerLiteral class
    public IntegerLiteral(Token token) {
        this.token = token;
    }


    public String tokenLiteral() {
        return token.Literal;
    }
    public Long getValue() {
        return value;
    }

}
