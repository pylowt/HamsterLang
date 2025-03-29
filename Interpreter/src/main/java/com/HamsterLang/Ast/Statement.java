package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class Statement implements ASTNode {

    private Identifier name;
    private final Token token;

    public Statement(Token token) {
        this.token = token;
    }

    public Statement(Token token, Identifier name) {
        this.token = token;
        this.setName(name);
    }

    public String tokenLiteral() {
        return token.Literal;
    }

    public String string() {
        return "";
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }
}
