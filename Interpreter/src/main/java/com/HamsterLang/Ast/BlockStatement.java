package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {
    public List<Statement> statements = new ArrayList<>();

    public BlockStatement(Token token) {
        super(token);
    }

    public String string() {
        var out = new StringBuilder();
        for (var statement : statements) {
            out.append(statement.string());
        }
        return out.toString();
    }

}
