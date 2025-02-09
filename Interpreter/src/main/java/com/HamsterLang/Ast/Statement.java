package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class Statement implements ASTNode {

    public Identifier name;
    private final Token token;

    public Statement(Token token) {
        this.token = token;
    }

    public Statement(Token token, Identifier name) {
        this.token = token;
        this.name = name;
    }

    public String tokenLiteral() {
        return token.Literal;
    }

    public String string() {
        return "";
    }

}
