package com.HamsterLang;

public class Lexer {
    private final String input;
    // Current reading position in input (after current char)
    private int readPosition;
    // Current char under examination
    private char ch;

    public Lexer(String input) {
        this.input = input;
        readChar();
    }

    public void readChar()
    {
        if (readPosition >= input.length())
        {
            ch = '\0';
        }
        else
        {
            ch = input.charAt(readPosition);
        }

        // Current position in input (points to current char)
        int position = readPosition;
        readPosition++;
    }

        private Token newToken(TokenTypes.TokenType tokenType)
        {
            return new Token(tokenType, String.valueOf(ch));
        }

        private Token newToken(TokenTypes.TokenType tokenType, String overrideString)
        {
            return new Token(tokenType, overrideString != null ? overrideString : String.valueOf(ch));
        }

        public Token nextToken()
        {
            Token tok;
            eatWhitespace();
            switch (ch) {
                case '=' -> {
                    if (peekAhead() == '=') {
                        tok = newToken(TokenTypes.TokenType.EQ, "==");
                        readChar();
                        break;
                    }
                    tok = newToken(TokenTypes.TokenType.ASSIGN);
                }
                case '+' -> tok = newToken(TokenTypes.TokenType.PLUS);
                case '-' -> tok = newToken(TokenTypes.TokenType.MINUS);
                case '!' -> {
                    if (peekAhead() == '=') {
                        tok = newToken(TokenTypes.TokenType.NOT_EQ, "!=");
                        readChar();
                        break;
                    }
                    tok = newToken(TokenTypes.TokenType.BANG);
                }
                case '*' -> tok = newToken(TokenTypes.TokenType.ASTERISK);
                case '/' -> tok = newToken(TokenTypes.TokenType.SLASH);
                case '<' -> tok = newToken(TokenTypes.TokenType.LT);
                case '>' -> tok = newToken(TokenTypes.TokenType.GT);
                case ',' -> tok = newToken(TokenTypes.TokenType.COMMA);
                case ';' -> tok = newToken(TokenTypes.TokenType.SEMICOLON);
                case '(' -> tok = newToken(TokenTypes.TokenType.LPAREN);
                case ')' -> tok = newToken(TokenTypes.TokenType.RPAREN);
                case '{' -> tok = newToken(TokenTypes.TokenType.LBRACE);
                case '}' -> tok = newToken(TokenTypes.TokenType.RBRACE);
                case '\0' -> tok = newToken(TokenTypes.TokenType.EOF, "");
                default -> {
                    if (isALetter()) {
                        var literal = readIdentifier();
                        tok = newToken(TokenTypes.lookupIdent(literal), literal);
                        return tok;
                    }
                    if (isADigit()) {
                        var literal = readNumber();
                        tok = newToken(TokenTypes.TokenType.INT, literal);
                        return tok;
                    }
                    tok = newToken(TokenTypes.TokenType.ILLEGAL);
                }
            }
            readChar();
            return tok;
        }
        private boolean isALetter()
        {
            return Character.isLetter(ch) || ch == '_' || ch == '!' || ch == '?';
        }
        private boolean isADigit()
        {
            return Character.isDigit(ch);
        }
        private String readIdentifier()
        {
            var sb = new StringBuilder();
            while (isALetter())
            {
                sb.append(ch);
                readChar();
            }
            return sb.toString();
        }
        private String readNumber()
        {
            var sb = new StringBuilder();
            while (isADigit())
            {
                sb.append(ch);
                readChar();
            }
            return sb.toString();
        }

        private void eatWhitespace()
        {
            while (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r')
            {
                readChar();
            }
        }

        private char peekAhead()
        {
            if (readPosition >= input.length())
            {
                return '\0';
            }

            return input.charAt(readPosition);
        }
    }
