package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class Statement implements ASTNode {

    public Identifier name;
    private Token token;

    public Statement(Token token){this.token = token;}
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
