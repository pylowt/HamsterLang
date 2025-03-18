import com.HamsterLang.Ast.*;
import com.HamsterLang.Lexer.Lexer;
import com.HamsterLang.Parser.Parser;
import com.HamsterLang.Tokens.Token;
import com.HamsterLang.Tokens.TokenTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ParserTests {
    private ASTRoot program;
    private Parser parser;

    public void initialise(String input) {
        var lexer = new Lexer(input);
        parser = new Parser(lexer);
        program = parser.parseProgram();
        checkParserErrors();
    }

    private void checkParserErrors() {
        var errors = parser.errors;
        assertFalse(errors.size() != 0, "Parser Error: " + String.join(" ", errors));
    }

    @Test
    void TestVarStatements() {
        var input = """
                var x = 5;
                var y = 10;
                var foobar = 838383;
                """;
        var expectedStatements = new String[]{"x", "y", "foobar"};
        initialise(input);
        var statements = program.statements;
        assertNotNull(statements);
        assertEquals(3, statements.size());
        for (int i = 0; i != expectedStatements.length; i++) {
            var statement = assertInstanceOf(VarStatement.class, statements.get(i));
            assertEquals(expectedStatements[i], statement.name.getValue());
            assertEquals("var", statement.tokenLiteral());
        }
    }

    @Test
    void TestReturnStatements() {
        var input = """
                return 5;
                return 10;
                return 993322;
                """;
        initialise(input);
        assertEquals(3, program.statements.size());
        for (var stmt : program.statements) {
            assertInstanceOf(ReturnStatement.class, stmt);
            assertEquals("return", stmt.tokenLiteral());
        }
    }

    @Test
    void TestStringRepresentation() {
        var varTok = new Token(TokenTypes.TokenType.VAR, "var");
        var someVar = new Identifier(new Token(TokenTypes.TokenType.IDENT, "myVar"), "myVar");
        var differentVar = new Identifier(
                new Token(TokenTypes.TokenType.IDENT, "differentVar"),
                "differentVar");
        var stmt = new VarStatement(varTok, someVar, differentVar);
        var program = new ASTRoot();
        program.statements.add(stmt);
        var expected = "var myVar = differentVar;";
        var actual = program.string();
        assertEquals(expected, actual);
    }

    @Test
    void TestIdentifierExpression() {
        var input = "foobar;";

        initialise(input);
        assertEquals(1, program.statements.size());
        var stmt = program.statements.get(0);
        assertInstanceOf(ExpressionStatement.class, stmt);
        var expressionStatement = (ExpressionStatement) stmt;
        var ident = (Identifier) expressionStatement.getExpression();
        assertInstanceOf(Identifier.class, ident);
        var value = ident.getValue();
        assertEquals("foobar", value);
        var tokenLiteral = ident.tokenLiteral();
        assertEquals("foobar", tokenLiteral);

    }
}