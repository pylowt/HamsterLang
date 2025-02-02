package com.HamsterLang;

import java.util.HashMap;
import java.util.Map;

public class TokenTypes {

    public enum TokenType {
        // Token types with their associated symbols
        EQ("=="),
        ILLEGAL("ILLEGAL"),
        EOF("EOF"),
        // Identifiers + literals
        IDENT("IDENT"),
        INT("INT"),
        // Operators
        ASSIGN("="),
        PLUS("+"),
        MINUS("-"),
        BANG("!"),
        ASTERISK("*"),
        SLASH("/"),
        NOT_EQ("!="),
        LT("<"),
        GT(">"),
        // Delimiters
        COMMA(","),
        SEMICOLON(";"),
        LPAREN("("),
        RPAREN(")"),
        LBRACE("{"),
        RBRACE("}"),
        // Keywords
        FUNCTION("FUNCTION"),
        VAR("VAR"),
        TRUE("TRUE"),
        FALSE("FALSE"),
        IF("IF"),
        ELSE("ELSE"),
        RETURN("RETURN");

        // Field to store the symbol associated with each token type
        private final String symbol;

        // Constructor to initialize the symbol for each token type
        TokenType(String symbol) {
            this.symbol = symbol;
        }

        // Getter method for the symbol
        public String getSymbol() {
            return symbol;
        }
    }

    // A static map to hold the keyword string -> TokenType mapping
    private static final Map<String, TokenType> Keywords = new HashMap<>();

    static {
        // Populating the map with keywords and their corresponding TokenType
        Keywords.put("func", TokenType.FUNCTION);
        Keywords.put("var", TokenType.VAR);
        Keywords.put("true", TokenType.TRUE);
        Keywords.put("false", TokenType.FALSE);
        Keywords.put("if", TokenType.IF);
        Keywords.put("else", TokenType.ELSE);
        Keywords.put("return", TokenType.RETURN);
    }

    // Method to lookup an identifier and return the corresponding TokenType
    public static TokenType lookupIdent(String ident) {
        if (Keywords.containsKey(ident)) {
            return Keywords.get(ident);  // Return the corresponding TokenType
        }
        return TokenType.IDENT;  // Return IDENT if not a keyword
    }
}