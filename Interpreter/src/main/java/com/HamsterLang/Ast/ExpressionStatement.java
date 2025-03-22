package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class ExpressionStatement extends Statement{
    public ExpressionStatement(Token token) {
        super(token);
    }
     private Expression expression;


    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String string() {
        if (expression != null) return expression.string();
        return "";
    }
}
