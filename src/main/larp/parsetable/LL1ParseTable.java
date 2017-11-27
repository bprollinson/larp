package larp.parsetable;

import larp.grammar.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.grammar.contextfreelanguage.NonTerminalNode;

import java.util.HashMap;

public class LL1ParseTable
{
    private ContextFreeGrammar contextFreeGrammar;
    private HashMap<NonTerminalNode, HashMap<ContextFreeGrammarSyntaxNode, Integer>> table;

    public LL1ParseTable(ContextFreeGrammar contextFreeGrammar)
    {
        this.contextFreeGrammar = contextFreeGrammar;
        this.table = new HashMap<NonTerminalNode, HashMap<ContextFreeGrammarSyntaxNode, Integer>>();
    }

    public void addCell(NonTerminalNode nonTerminalNode, ContextFreeGrammarSyntaxNode terminalNode, int contextFreeGrammarRuleIndex)
    {
        HashMap<ContextFreeGrammarSyntaxNode, Integer> entry = this.table.get(nonTerminalNode);
        if (entry == null)
        {
            entry = new HashMap<ContextFreeGrammarSyntaxNode, Integer>();
        }

        entry.put(terminalNode, contextFreeGrammarRuleIndex);
        this.table.put(nonTerminalNode, entry);
    }

    public Integer getCell(NonTerminalNode nonTerminalNode, ContextFreeGrammarSyntaxNode terminalNode)
    {
        Integer position = null;
        HashMap<ContextFreeGrammarSyntaxNode, Integer> entry = this.table.get(nonTerminalNode);
        if (entry != null)
        {
            position = entry.get(terminalNode);
        }

        return position;
    }

    public ContextFreeGrammar getContextFreeGrammar()
    {
        return this.contextFreeGrammar;
    }

    public HashMap<NonTerminalNode, HashMap<ContextFreeGrammarSyntaxNode, Integer>> getTable()
    {
        return this.table;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof LL1ParseTable))
        {
            return false;
        }

        if (!this.contextFreeGrammar.equals(((LL1ParseTable)other).getContextFreeGrammar()))
        {
            return false;
        }

        return this.table.equals(((LL1ParseTable)other).getTable());
    }
}
