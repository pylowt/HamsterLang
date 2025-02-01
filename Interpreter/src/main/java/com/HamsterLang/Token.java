package com.HamsterLang;

public class Token {
    public  TokenTypes.TokenType Type;
    public  String Literal;

    public Token(TokenTypes.TokenType type, String literal)
    {
        Type = type;
        Literal = literal;
    }


}
