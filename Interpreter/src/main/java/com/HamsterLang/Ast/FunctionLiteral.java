package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

import java.util.List;

public class FunctionLiteral extends Expression{
    private final Token token; // The 'fn' token
    private final List<Identifier> parameters;
    private BlockStatement body;

    public FunctionLiteral(List<Identifier> parameters, Token token) {
        this.parameters = parameters;
        this.token = token;
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String tokenLiteral() {
        return token.Literal;
    }

    @Override
    public String string() {
        StringBuilder out = new StringBuilder();
        out.append(tokenLiteral());
        out.append("(");
        for (int i = 0; i < parameters.size(); i++) {
            out.append(parameters.get(i).string());
            if (i != parameters.size() - 1) {
                out.append(", ");
            }
        }
        out.append(") ");
        out.append(body.string());
        return out.toString();
    }
}
