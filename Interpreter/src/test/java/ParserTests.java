import com.HamsterLang.Ast.ASTRoot;
import com.HamsterLang.Ast.Identifier;
import com.HamsterLang.Ast.ReturnStatement;
import com.HamsterLang.Ast.VarStatement;
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
            assertEquals(expectedStatements[i], statement.name.value);
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
}