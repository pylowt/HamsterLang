import java.util.List;

public class FunctionParsingTestCase extends ParserTestCase {
        List<String> parameters;

        FunctionParsingTestCase(String input, List<String> parameters) {
            super(input);
            this.parameters = parameters;
        }
    }
