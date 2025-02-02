import com.HamsterLang.Tokens.TokenTypes;
import com.HamsterLang.Lexer.Lexer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTests {

    @Test
    @DisplayName("Lexer gets correct token?")
    void lexerGetsNextToken() {
        var input = """
                var five = 5;
                var ten = 10;
                var add_? = func(x, y) {
                    x + y;
                };
                var result! = add(five, ten);
                !-/*5;
                5 < 10 > 5;
                		
                if (5 < 10) {
                    return true;
                } else {
                    return false;
                }

                10 == 10;
                10 != 9;

                """;
        Map<TokenTypes.TokenType, String>[] tests = new Map[]{
                Collections.singletonMap(TokenTypes.TokenType.VAR, "var"),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "five"),
                Collections.singletonMap(TokenTypes.TokenType.ASSIGN, "="),
                Collections.singletonMap(TokenTypes.TokenType.INT, "5"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.VAR, "var"),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "ten"),
                Collections.singletonMap(TokenTypes.TokenType.ASSIGN, "="),
                Collections.singletonMap(TokenTypes.TokenType.INT, "10"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.VAR, "var"),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "add_?"),
                Collections.singletonMap(TokenTypes.TokenType.ASSIGN, "="),
                Collections.singletonMap(TokenTypes.TokenType.FUNCTION, "func"),
                Collections.singletonMap(TokenTypes.TokenType.LPAREN, "("),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "x"),
                Collections.singletonMap(TokenTypes.TokenType.COMMA, ","),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "y"),
                Collections.singletonMap(TokenTypes.TokenType.RPAREN, ")"),
                Collections.singletonMap(TokenTypes.TokenType.LBRACE, "{"),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "x"),
                Collections.singletonMap(TokenTypes.TokenType.PLUS, "+"),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "y"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.RBRACE, "}"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.VAR, "var"),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "result!"),
                Collections.singletonMap(TokenTypes.TokenType.ASSIGN, "="),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "add"),
                Collections.singletonMap(TokenTypes.TokenType.LPAREN, "("),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "five"),
                Collections.singletonMap(TokenTypes.TokenType.COMMA, ","),
                Collections.singletonMap(TokenTypes.TokenType.IDENT, "ten"),
                Collections.singletonMap(TokenTypes.TokenType.RPAREN, ")"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.BANG, "!"),
                Collections.singletonMap(TokenTypes.TokenType.MINUS, "-"),
                Collections.singletonMap(TokenTypes.TokenType.SLASH, "/"),
                Collections.singletonMap(TokenTypes.TokenType.ASTERISK, "*"),
                Collections.singletonMap(TokenTypes.TokenType.INT, "5"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.INT, "5"),
                Collections.singletonMap(TokenTypes.TokenType.LT, "<"),
                Collections.singletonMap(TokenTypes.TokenType.INT, "10"),
                Collections.singletonMap(TokenTypes.TokenType.GT, ">"),
                Collections.singletonMap(TokenTypes.TokenType.INT, "5"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.IF, "if"),
                Collections.singletonMap(TokenTypes.TokenType.LPAREN, "("),
                Collections.singletonMap(TokenTypes.TokenType.INT, "5"),
                Collections.singletonMap(TokenTypes.TokenType.LT, "<"),
                Collections.singletonMap(TokenTypes.TokenType.INT, "10"),
                Collections.singletonMap(TokenTypes.TokenType.RPAREN, ")"),
                Collections.singletonMap(TokenTypes.TokenType.LBRACE, "{"),
                Collections.singletonMap(TokenTypes.TokenType.RETURN, "return"),
                Collections.singletonMap(TokenTypes.TokenType.TRUE, "true"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.RBRACE, "}"),
                Collections.singletonMap(TokenTypes.TokenType.ELSE, "else"),
                Collections.singletonMap(TokenTypes.TokenType.LBRACE, "{"),
                Collections.singletonMap(TokenTypes.TokenType.RETURN, "return"),
                Collections.singletonMap(TokenTypes.TokenType.FALSE, "false"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.RBRACE, "}"),
                Collections.singletonMap(TokenTypes.TokenType.INT, "10"),
                Collections.singletonMap(TokenTypes.TokenType.EQ, "=="),
                Collections.singletonMap(TokenTypes.TokenType.INT, "10"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.INT, "10"),
                Collections.singletonMap(TokenTypes.TokenType.NOT_EQ, "!="),
                Collections.singletonMap(TokenTypes.TokenType.INT, "9"),
                Collections.singletonMap(TokenTypes.TokenType.SEMICOLON, ";"),
                Collections.singletonMap(TokenTypes.TokenType.EOF, ""),
        };

        var lexer = new Lexer(input);
        for (var entry : tests)
        {
            var result = lexer.nextToken();
            var entryKey = entry.keySet().iterator().next();
            var typeMessage = result.Type + " - TokenType wrong. Expected " + entryKey;
            assertEquals(entryKey, result.Type, typeMessage);
            var entryValue = entry.values().iterator().next();
            var literalMessage = result.Literal + " - Literal wrong. Expected " + entryValue;
            assertEquals(entryValue, result.Literal, literalMessage);
        }

    }
}
