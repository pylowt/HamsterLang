import com.HamsterLang.Ast.*;
import com.HamsterLang.Ast.BooleanLiteral;
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
        parser.setTracingEnabled(false);
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
            assertEquals(expectedStatements[i], statement.getName().getValue());
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

    @Test
    void TestBooleanExpression() {
        var input = "true;";

        initialise(input);

        assertEquals(1, program.statements.size());

        var stmt = program.statements.get(0);

        assertInstanceOf(ExpressionStatement.class, stmt);

        var expressionStatement = (ExpressionStatement) stmt;
        var booleanLiteral = (BooleanLiteral) expressionStatement.getExpression();

        assertInstanceOf(BooleanLiteral.class, booleanLiteral);

        boolean value = booleanLiteral.getValue();

        assertTrue(value);

        var tokenLiteral = booleanLiteral.tokenLiteral();

        assertEquals("true", tokenLiteral);

    }

    static Stream<InfixPrefixTestCase> prefixTestCases() {
        return Stream.of(
                new InfixPrefixTestCase("!5;", "!", List.of(5L)),
                new InfixPrefixTestCase("-15;", "-", List.of(15L)),
                new InfixPrefixTestCase("!true;", "!", List.of(true)),
                new InfixPrefixTestCase("!false;", "!", List.of(false))
        );
    }

    @ParameterizedTest
    @MethodSource("prefixTestCases")
    void testParsingPrefixExpressions(InfixPrefixTestCase tt) {
        initialise(tt.input);

        // Assert that there is exactly one statement in the program
        assertEquals(1, program.statements.size(), "program.Statements does not contain 1 statement");

        var stmt = (ExpressionStatement) program.statements.get(0);

        assertTrue(stmt.getExpression() instanceof PrefixExpression,
                "exp is not a PrefixExpression");

        PrefixExpression exp = (PrefixExpression) stmt.getExpression();

        assertEquals(tt.operator, exp.getOperator(), "exp.Operator does not match expected value");

        testLiteralExpression(exp.getRight(), tt.operands.get(0));
    }


    static Stream<InfixPrefixTestCase> infixTestCases() {
        return Stream.of(
                new InfixPrefixTestCase("5 + 5;", "+", List.of(5L, 5L)),
                new InfixPrefixTestCase("5 - 5;", "-", List.of(5L, 5L)),
                new InfixPrefixTestCase("5 * 5;", "*", List.of(5L, 5L)),
                new InfixPrefixTestCase("5 / 5;", "/", List.of(5L, 5L)),
                new InfixPrefixTestCase("5 > 5;", ">", List.of(5L, 5L)),
                new InfixPrefixTestCase("5 < 5;", "<", List.of(5L, 5L)),
                new InfixPrefixTestCase("5 == 5;", "==", List.of(5L, 5L)),
                new InfixPrefixTestCase("5 != 5;", "!=", List.of(5L, 5L)),
                new InfixPrefixTestCase("true == true", "==", List.of(true, true)),
                new InfixPrefixTestCase("true != false", "!=", List.of(true, false)),
                new InfixPrefixTestCase("false == false", "==", List.of(false, false))
        );
    }

    @ParameterizedTest
    @MethodSource("infixTestCases")
    void testParsingInfixExpressions(InfixPrefixTestCase tt) {
        initialise(tt.input);

        // Assert that there is exactly one statement in the program
        assertEquals(1, program.statements.size(), "program.Statements does not contain 1 statement");

        var stmt = (ExpressionStatement) program.statements.get(0);

        assertTrue(stmt.getExpression() instanceof InfixExpression,
                "exp is not a InfixExpression");

        InfixExpression exp = (InfixExpression) stmt.getExpression();

        assertEquals(tt.operator, exp.getOperator(), "exp.Operator does not match expected value");
        if (tt.operands.get(0) instanceof Long left) {
            testIntegerLiteral(exp.getLeft(), left);
        }
        if (tt.operands.get(1) instanceof Long right) {
            testIntegerLiteral(exp.getRight(), right);
        }

        testInfixExpression(exp, exp.getLeft(), exp.getOperator(), exp.getRight());
    }

    static Stream<OperatorPrecedenceTestCase> operatorPrecedenceTestCases() {
        return Stream.of(
                new OperatorPrecedenceTestCase("a == b", "(a == b)"),
                new OperatorPrecedenceTestCase("-a * b", "((-a) * b)"),
                new OperatorPrecedenceTestCase("!-a", "(!(-a))"),
                new OperatorPrecedenceTestCase("a + b + c", "((a + b) + c)"),
                new OperatorPrecedenceTestCase("a + b - c", "((a + b) - c)"),
                new OperatorPrecedenceTestCase("a * b * c", "((a * b) * c)"),
                new OperatorPrecedenceTestCase("a * b / c", "((a * b) / c)"),
                new OperatorPrecedenceTestCase("a + b / c", "(a + (b / c))"),
                new OperatorPrecedenceTestCase("a + b * c + d / e - f",
                        "(((a + (b * c)) + (d / e)) - f)"),
                new OperatorPrecedenceTestCase("3 + 4; -5 * 5", "(3 + 4)((-5) * 5)"),
                new OperatorPrecedenceTestCase("5 > 4 == 3 < 4", "((5 > 4) == (3 < 4))"),
                new OperatorPrecedenceTestCase("5 < 4 != 3 > 4", "((5 < 4) != (3 > 4))"),
                new OperatorPrecedenceTestCase("3 + 4 * 5 == 3 * 1 + 4 * 5",
                        "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"),
                new OperatorPrecedenceTestCase("true", "true"),
                new OperatorPrecedenceTestCase("false", "false"),
                new OperatorPrecedenceTestCase("3 > 5 == false", "((3 > 5) == false)"),
                new OperatorPrecedenceTestCase("3 < 5 == true", "((3 < 5) == true)"),
                new OperatorPrecedenceTestCase("true == true", "(true == true)"),
                new OperatorPrecedenceTestCase("false == false", "(false == false)"),
                new OperatorPrecedenceTestCase("true != false", "(true != false)"),
                new OperatorPrecedenceTestCase("1 + (2 + 3) + 4", "((1 + (2 + 3)) + 4)"),
                new OperatorPrecedenceTestCase("(5 + 5) * 2", "((5 + 5) * 2)"),
                new OperatorPrecedenceTestCase("2 / (5 + 5)", "(2 / (5 + 5))"),
                new OperatorPrecedenceTestCase("-(5 + 5)", "(-(5 + 5))"),
                new OperatorPrecedenceTestCase("!(true == true)", "(!(true == true))")
        );
    }

    @ParameterizedTest
    @MethodSource("operatorPrecedenceTestCases")
    void testOperatorPrecedenceParsing(OperatorPrecedenceTestCase tt) {

        initialise(tt.input);

        assertEquals(tt.expected, program.string());
    }

    @Test
    void TestIfExpression() {
        var input = "if (x < y) { x }";

        initialise(input);

        assertEquals(1, program.statements.size());

        var stmt = program.statements.get(0);

        assertInstanceOf(ExpressionStatement.class, stmt);

        var expressionStatement = (ExpressionStatement) stmt;

        var ifExpression = (IfExpression) expressionStatement.getExpression();

        assertInstanceOf(IfExpression.class, ifExpression);

        testInfixExpression(ifExpression.getCondition(), "x", "<", "y");

        assertEquals(1, ifExpression.getConsequence().statements.size());

        var consequence = ifExpression.getConsequence().statements.get(0);

        assertInstanceOf(ExpressionStatement.class, consequence);

        var consequenceExpression = (ExpressionStatement) consequence;

        testIdentifier(consequenceExpression.getExpression(), "x");

        assertNull(ifExpression.getAlternative());
    }

    @Test
    void testIfElseExpression() {
        var input = "if (x < y) { x } else { y }";

        initialise(input);

        assertEquals(1, program.statements.size());

        var stmt = program.statements.get(0);

        assertInstanceOf(ExpressionStatement.class, stmt);

        var expressionStatement = (ExpressionStatement) stmt;

        var ifExpression = (IfExpression) expressionStatement.getExpression();

        assertInstanceOf(IfExpression.class, ifExpression);

        testInfixExpression(ifExpression.getCondition(), "x", "<", "y");

        assertEquals(1, ifExpression.getConsequence().statements.size());

        var consequence = ifExpression.getConsequence().statements.get(0);

        assertInstanceOf(ExpressionStatement.class, consequence);

        var consequenceExpression = (ExpressionStatement) consequence;

        testIdentifier(consequenceExpression.getExpression(), "x");

        assertNotNull(ifExpression.getAlternative());

        assertEquals(1, ifExpression.getAlternative().statements.size());

        var alternative = ifExpression.getAlternative().statements.get(0);

        assertInstanceOf(ExpressionStatement.class, alternative);

        var alternativeExpression = (ExpressionStatement) alternative;

        testIdentifier(alternativeExpression.getExpression(), "y");
    }

    @Test
    void TestFunctionLiteralParsing() {
        var input = "func(x, y) { x + y; }";

        initialise(input);

        assertEquals(1, program.statements.size());

        var stmt = program.statements.get(0);

        assertInstanceOf(ExpressionStatement.class, stmt);

        var expressionStatement = (ExpressionStatement) stmt;

        var function = (FunctionLiteral) expressionStatement.getExpression();

        assertInstanceOf(FunctionLiteral.class, function);

        assertEquals(2, function.getParameters().size(), "Function literal parameters wrong. Want 2, got " +
                function.getParameters().size());

        testLiteralExpression(function.getParameters().get(0), "x");

        testLiteralExpression(function.getParameters().get(1), "y");

        assertEquals(1, function.getBody().statements.size(), "Function body statements wrong. Want 1, got " +
                function.getBody().statements.size());

        var bodyStmt = function.getBody().statements.get(0);

        assertInstanceOf(ExpressionStatement.class, bodyStmt);

        testInfixExpression(((ExpressionStatement) bodyStmt).getExpression(), "x", "+", "y");
    }

    static Stream<FunctionParsingTestCase> functionParameterTestCases() {
        return Stream.of(
                new FunctionParsingTestCase("func() {}", List.of()),
                new FunctionParsingTestCase("func(x) {}", List.of("x")),
                new FunctionParsingTestCase("func(x, y, z) {}", List.of("x", "y", "z"))
        );
    }

    @ParameterizedTest
    @MethodSource("functionParameterTestCases")
    void TestFunctionParameterParsing(FunctionParsingTestCase tt) {

        initialise(tt.input);

        assertEquals(1, program.statements.size());

        var stmt = program.statements.get(0);

        assertInstanceOf(ExpressionStatement.class, stmt);

        var expressionStatement = (ExpressionStatement) stmt;

        var function = (FunctionLiteral) expressionStatement.getExpression();

        assertInstanceOf(FunctionLiteral.class, function);

        assertEquals(function.getParameters().size(), tt.parameters.size(), "Length of parameters wrong. Want " +
                tt.parameters.size() + ", got " + function.getParameters().size());
    }

    void testIntegerLiteral(Expression il, long value) {

        assertInstanceOf(IntegerLiteral.class, il, "Expression is not an IntegerLiteral");

        IntegerLiteral integ = (IntegerLiteral) il;  // Safe cast after assertion

        assertEquals(value, integ.getValue(), "IntegerLiteral value does not match expected");

        assertEquals(String.format("%d", value), integ.tokenLiteral(), "Token literal does not match " +
                "expected value");
    }

    void testIdentifier(Expression exp, String value) {

        assertInstanceOf(Identifier.class, exp, "Expression is not an Identifier");

        Identifier ident = (Identifier) exp;  // Safe cast after assertion

        assertEquals(value, ident.getValue());

        assertEquals(value, ident.tokenLiteral());
    }

    void testLiteralExpression(Expression exp, Object expected) {
        if (expected instanceof IntegerLiteral expectedInt) {
            testIntegerLiteral(exp, expectedInt.getValue());
        }
        if (expected instanceof String expectedString) {
             testIdentifier(exp, expectedString);
        }
        if (expected instanceof BooleanLiteral expectedBoolean) {
            testBooleanLiteral(exp, expectedBoolean.getValue());
        }
    }

    void testInfixExpression(Expression exp, Object left, String operator, Object right) {

        assertInstanceOf(InfixExpression.class, exp, "Expression not an InfixExpression, got " +
                exp.getClass().getName());

        var infix = (InfixExpression) exp; // Safe cast after assertion

        testLiteralExpression(infix.getLeft(), left);

        assertEquals(infix.getOperator(), operator);

        testLiteralExpression(infix.getRight(), right);
    }

    void testBooleanLiteral(Expression exp, boolean value) {
        assertInstanceOf(BooleanLiteral.class, exp, "Expression not a BooleanLiteral, got " +
                exp.getClass().getName());

        BooleanLiteral bool = (BooleanLiteral) exp;  // Safe cast after assertion

        assertEquals(value, bool.getValue(), "BooleanLiteral value not " + value + " got " + bool.getValue());

        assertEquals(String.valueOf(value), bool.tokenLiteral(), "Token literal not " + value + " got " +
                bool.tokenLiteral());
    }
}
