package com.HamsterLang.Parser;
import com.HamsterLang.Ast.Expression;

@FunctionalInterface
interface InfixParseFn {
    Expression parse(Expression expr);  // Method with one argument (Expression), returning an Expression
}

