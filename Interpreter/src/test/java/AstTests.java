import com.HamsterLang.Ast.ASTRoot;
import com.HamsterLang.Ast.Identifier;
import com.HamsterLang.Ast.Statement;
import com.HamsterLang.Ast.VarStatement;
import com.HamsterLang.Tokens.Token;
import com.HamsterLang.Tokens.TokenTypes;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AstTests {

    @Test
    void testString() {
        var program = new ASTRoot();
        ArrayList<Statement> statements = new ArrayList<>();
        var varStatement = new VarStatement(
                new Token(TokenTypes.TokenType.VAR, "var"),
                new Identifier(new Token(TokenTypes.TokenType.IDENT, "myVar"), "myVar"),
                new Identifier(new Token(TokenTypes.TokenType.IDENT, "anotherVar"), "anotherVar")) ;
        statements.add(varStatement);
        program.statements = statements;
        var actual = program.string();
        assertEquals("var myVar = anotherVar;", actual);
    }

}
