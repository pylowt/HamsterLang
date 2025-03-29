package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class VarStatement extends Statement {
    public ASTNode value = null;

    public VarStatement(Token token)
    {
        super(token);
    }

    public VarStatement(Token token, Identifier name, ASTNode value) {
        super(token, name);
        this.value = value;
    }

    public String string()
    {
        var outString = tokenLiteral() + " ";
        if (getName() != null) outString += getName().string();
        outString += " = ";
        if (value != null) outString += value.string();
        outString += ";";
        return outString;
    }
}
