import com.HamsterLang.Ast.*;
import com.HamsterLang.Lexer.Lexer;
import com.HamsterLang.Parser.Parser;
import com.HamsterLang.Tokens.Token;
import com.HamsterLang.Tokens.TokenTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

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
    @Test
    void TestIntegerLiteralExpression() {
        var input = "5;";

        initialise(input);
        assertEquals(1, program.statements.size());
        var stmt = program.statements.get(0);
        assertInstanceOf(ExpressionStatement.class, stmt);
        var expressionStatement = (ExpressionStatement) stmt;
        var intLiteral = (IntegerLiteral) expressionStatement.getExpression();
        assertInstanceOf(IntegerLiteral.class, intLiteral);
        long value = intLiteral.getValue();
        assertEquals(5, value);
        var tokenLiteral = intLiteral.tokenLiteral();
        assertEquals("5", tokenLiteral);

    }


        // Test case class to hold input, expected operator, and expected integer value
        static class TestCase {
            String input;
            String operator;
            List<Integer> operands;

            TestCase(String input, String operator, List<Integer> operands) {
                this.input = input;
                this.operator = operator;
                this.operands = operands;
            }
        }

    static Stream<TestCase> prefixTestCases() {
        return Stream.of(
                new TestCase("!5;", "!", List.of(5)),
                new TestCase("-15;", "-", List.of(15))
        );
    }

        @ParameterizedTest
        @MethodSource("prefixTestCases")
        void testParsingPrefixExpressions(TestCase tt) {
            initialise(tt.input);

            // Assert that there is exactly one statement in the program
            assertEquals(1, program.statements.size(), "program.Statements does not contain 1 statement");

            var stmt = (ExpressionStatement) program.statements.get(0);
            assertTrue(stmt.getExpression() instanceof PrefixExpression,
                    "exp is not a PrefixExpression");

            PrefixExpression exp = (PrefixExpression) stmt.getExpression();
            assertEquals(tt.operator, exp.getOperator(), "exp.Operator does not match expected value");

            assertTrue(testIntegerLiteral(exp.getRight(), tt.operands.get(0)));
        }

        boolean testIntegerLiteral(Expression il, long value) {
            if (!(il instanceof IntegerLiteral integ)) {
                return false;
            }
            if (integ.getValue() != value) {
                return false;
            }

            return integ.tokenLiteral().equals(String.format("%d", value));
        }

        static Stream<TestCase> infixTestCases() {
            return Stream.of(
                    new TestCase("5 + 5;", "+", List.of(5, 5)),
                    new TestCase("5 - 5;", "-", List.of(5, 5)),
                    new TestCase("5 * 5;", "*", List.of(5, 5)),
                    new TestCase("5 / 5;", "/", List.of(5, 5)),
                    new TestCase("5 > 5;", ">", List.of(5, 5)),
                    new TestCase("5 < 5;", "<", List.of(5, 5)),
                    new TestCase("5 == 5;", "==", List.of(5, 5)),
                    new TestCase("5 != 5;", "!=", List.of(5, 5))
            );
        }

        @ParameterizedTest
        @MethodSource("infixTestCases")
        void testParsingInfixExpressions(TestCase tt) {
            initialise(tt.input);

            // Assert that there is exactly one statement in the program
            assertEquals(1, program.statements.size(), "program.Statements does not contain 1 statement");

            var stmt = (ExpressionStatement) program.statements.get(0);
            assertTrue(stmt.getExpression() instanceof InfixExpression,
                    "exp is not a InfixExpression");

            InfixExpression exp = (InfixExpression) stmt.getExpression();
            assertEquals(tt.operator, exp.getOperator(), "exp.Operator does not match expected value");

            assertTrue(testIntegerLiteral(exp.getLeft(), tt.operands.get(0)));
            assertTrue(testIntegerLiteral(exp.getRight(), tt.operands.get(1)));
        }
}

