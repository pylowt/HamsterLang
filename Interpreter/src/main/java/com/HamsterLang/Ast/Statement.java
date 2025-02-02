package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class Statement implements ASTNode {

    private Token token;

    public String tokenLiteral()
    {
        return token.Literal;
    }

    public String string()
    {
        return "";
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
