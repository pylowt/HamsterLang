import com.HamsterLang.Ast.ASTRoot;
import com.HamsterLang.Ast.VarStatement;
import com.HamsterLang.Lexer.Lexer;
import com.HamsterLang.Parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ParserTests {
    private ASTRoot program;
    private Parser parser;

    public void initialise(String input) {
        var lexer = new Lexer(input);
        parser = new Parser(lexer);
        program = parser.parseProgram();
        CheckParserErrors();
    }
    private void CheckParserErrors()
    {
        var errors = parser.errors;
        assertFalse(errors.size() != 0, "Parser Error: " + String.join(" ", errors));
    }

    @Test
    void TestVarStatements(){
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
        for (int i = 0 ; i != expectedStatements.length ; i++) {
            var statement = assertInstanceOf(VarStatement.class, statements.get(i));
            assertEquals(expectedStatements[i], statement.name.value);
            assertEquals("var", statement.tokenLiteral());
        }
    }

}
