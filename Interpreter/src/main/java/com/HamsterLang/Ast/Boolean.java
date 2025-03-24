package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class Boolean extends Expression{
    private final Token token;
    private final boolean value;

    public Boolean(Token token, boolean value) {
        this.token = token;
        this.value = value;
    }

    public String tokenLiteral() {
        return token.Literal;
    }

    public String string() {
        return token.Literal;
    }

    public boolean getValue() {
        return this.value;
    }
}
