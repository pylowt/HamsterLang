class OperatorPrecedenceTestCase extends ParserTestCase {
    String expected;

    OperatorPrecedenceTestCase(String input, String expected) {
        super(input);
        this.expected = expected;
    }
}
