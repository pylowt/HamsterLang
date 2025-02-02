package com.HamsterLang.Ast;

public class ASTRoot implements ASTNode {

	public Statement[] statements;

	public String tokenLiteral()
	{
		if (statements.length > 0)
			return statements[0].tokenLiteral();
		return "";
	}

	public String string()
	{
        var sb = new StringBuilder();

		for (var statement : statements)
		{
			sb.append(statement.string());
		}

		return sb.toString();
	}
}

