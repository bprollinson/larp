package larp.parser.contextfreelanguage;

import larp.assertion.Assertion;
import larp.parser.regularlanguage.State;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarSyntaxNode;

public class LR0ParseTableCellAvailableAssertion implements Assertion
{
    public LR0ParseTableCellAvailableAssertion(LR0ParseTable parseTable, State state, ContextFreeGrammarSyntaxNode syntaxNode, LR0ParseTableAction action)
    {
    }

    public void validate()
    {
    }
}
