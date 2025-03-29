package com.HamsterLang.Ast;

import com.HamsterLang.Tokens.Token;

public class IfExpression extends Expression {
    private final Token token; // The 'if' token
    private Expression condition;
    private BlockStatement consequence;
    private BlockStatement alternative;

    public IfExpression(Token token) {
        this.token = token;
    }


    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public BlockStatement getConsequence() {
        return consequence;
    }

    public void setConsequence(BlockStatement consequence) {
        this.consequence = consequence;
    }

    public BlockStatement getAlternative() {
        return alternative;
    }

    public void setAlternative(BlockStatement alternative) {
        this.alternative = alternative;
    }

    @Override
    public String tokenLiteral() {
        return token.Literal;
    }

    @Override
    public String string() {
        StringBuilder out = new StringBuilder();
        out.append("if ");
        out.append(condition.string());
        out.append(" ");
        out.append(consequence.string());
        if (alternative != null) {
            out.append(" else ");
            out.append(alternative.string());
        }
        return out.toString();
    }
}
