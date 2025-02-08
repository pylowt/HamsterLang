package com.HamsterLang.Ast;

import java.util.ArrayList;

public class ASTRoot implements ASTNode {

	public ArrayList<Statement> statements = new ArrayList<>();

	public String tokenLiteral()
	{
		if (statements.size() > 0)
			return statements.get(0).tokenLiteral();
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

