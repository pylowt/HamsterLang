package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class Identifier extends Expression {
    private final Token token;

    private String value;

    public Identifier(Token token, String value)
    {
        this.token = token;
        this.setValue(value);
    }

    public String tokenLiteral()
    {
        return token.Literal;
    }

    public String string()
    {
        return getValue();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
