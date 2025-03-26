package com.HamsterLang.Tokens;

/** Token class is responsible for holding the token type and the literal value of the token.
 * It is used by the lexer to generate tokens from user input. TokenTypes are defined in TokenTypes enum property.
 */
public class Token {
    public TokenTypes.TokenType Type;
    public  String Literal;

    public Token(TokenTypes.TokenType type, String literal)
    {
        Type = type;
        Literal = literal;
    }


}
