package com.HamsterLang.Parser;
import com.HamsterLang.Ast.Expression;

@FunctionalInterface
interface PrefixParseFn {
    Expression parse();  // Method with no arguments, returning an Expression
}
