package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class VarStatement extends Statement {
    public ASTNode value = null;

    public VarStatement(Token token)
    {
        super(token);
    }

    public String string()
    {
        var outString = tokenLiteral() + " ";
        if (name != null) outString += name.string();
        outString += " = ";
        if (value != null) outString += value.string();
        outString += ";";
        return outString;
    }
}
