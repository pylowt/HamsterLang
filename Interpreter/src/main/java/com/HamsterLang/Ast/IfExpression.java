package com.HamsterLang.Ast;

public class IfExpression extends Expression {
    public ASTRoot getConsequence() {
        return new ASTRoot();
    }

    public Expression getCondition() {
        return new Expression();
    }

    public Expression getAlternative() {
        return new Expression();
    }
}
