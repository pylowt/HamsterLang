package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class ReturnStatement extends Statement
    {
        ASTNode ReturnValue = null;

        public ReturnStatement(Token token) {
            super(token);
        }

        public String string()
        {
            var outString = tokenLiteral() + " ";
            if (ReturnValue != null) outString += ReturnValue.string();
            outString += ";";
            return outString;
        }
    }

