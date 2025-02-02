package com.HamsterLang;

import com.HamsterLang.Lexer.Lexer;
import com.HamsterLang.Tokens.TokenTypes;

public class Repl {
    public static void processInput(String input)
    {
        var lexer = new Lexer(input);
        var tok = lexer.nextToken();
        while (tok.Type != TokenTypes.TokenType.EOF)
        {
            System.out.println("Type:" + tok.Type + " Literal:" + tok.Literal);
            tok = lexer.nextToken();
        }
    }
}
