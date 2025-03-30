package com.HamsterLang.Ast;

public class CallExpression extends Expression {
    private final Expression function;
    private final Expression[] arguments;

    public CallExpression(Expression function, Expression[] arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public Expression getFunction() {
        return function;
    }

    public Expression[] getArguments() {
        return arguments;
    }

    @Override
    public String string() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(function.string());
        for (Expression argument : arguments) {
            sb.append(", ");
            sb.append(argument.string());
        }
        sb.append(")");
        return sb.toString();
    }
}
