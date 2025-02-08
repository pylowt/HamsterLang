package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class Identifier {
    private final Token token;

    public String value;

    public Identifier(Token token, String value)
    {
        this.token = token;
        this.value = value;
    }

    public String tokenLiteral()
    {
        return token.Literal;
    }

    public String string()
    {
        return value;
    }
}
