import java.util.List;

class InfixPrefixTestCase extends ParserTestCase {
    String operator;
    List<Object> operands;

    InfixPrefixTestCase(String input, String operator, List<Object> operands) {
        super(input);
        this.operator = operator;
        this.operands = operands;
    }
}
