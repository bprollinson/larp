package larp.parsetable;

import larp.grammar.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.grammar.contextfreelanguage.NonTerminalNode;
import larp.grammar.contextfreelanguage.TerminalNode;

import java.util.Vector;

public class LL1ParseTable
{
    private ContextFreeGrammar contextFreeGrammar;
    private Vector<NonTerminalNode> nonTerminalNodes;
    private Vector<TerminalNode> terminalNodes;
    private Vector<Integer> contextFreeGrammarRuleIndexes;

    public LL1ParseTable(ContextFreeGrammar contextFreeGrammar)
    {
        this.contextFreeGrammar = contextFreeGrammar;
        this.nonTerminalNodes = new Vector<NonTerminalNode>();
        this.terminalNodes = new Vector<TerminalNode>();
        this.contextFreeGrammarRuleIndexes = new Vector<Integer>();
    }

    public boolean accepts(String inputString)
    {
        Vector<ContextFreeGrammarSyntaxNode> stack = new Vector<ContextFreeGrammarSyntaxNode>();
        stack.add(contextFreeGrammar.getStartSymbol());

        String remainingInput = inputString;

        while (remainingInput.length() > 0 && stack.size() > 0)
        {
            ContextFreeGrammarSyntaxNode topNode = stack.get(0);
            String nextCharacter = remainingInput.substring(0, 1);

            if (topNode instanceof NonTerminalNode)
            {
                int position = -1;
                for (int i = 0; i < this.nonTerminalNodes.size(); i++)
                {
                    if (this.nonTerminalNodes.get(i).equals(topNode) && this.terminalNodes.get(i).getValue().equals(nextCharacter))
                    {
                        position = i;
                        break;
                    }
                }

                stack.remove(0);

                if (position == -1)
                {
                    return false;
                }

                Vector<ContextFreeGrammarSyntaxNode> childNodes = this.contextFreeGrammar.getProduction(position).getChildNodes().get(1).getChildNodes();
                for (int i = childNodes.size() - 1; i >= 0; i--)
                {
                    stack.add(0, childNodes.get(i));
                }
            }
            else
            {
                if (!(((TerminalNode)topNode).getValue().substring(0, 1).equals(nextCharacter)))
                {
                    return false;
                }

                stack.remove(0);

                String remainingTerminalContent = ((TerminalNode)topNode).getValue().substring(1);
                if (remainingTerminalContent.length() > 0)
                {
                    stack.add(0, new TerminalNode(remainingTerminalContent));
                }

                remainingInput = remainingInput.substring(1);
            }
        }

        return remainingInput.length() == 0 && stack.size() == 0;
    }

    public void addCell(NonTerminalNode nonTerminalNode, TerminalNode terminalNode, int contextFreeGrammarRuleIndex)
    {
        this.nonTerminalNodes.add(nonTerminalNode);
        this.terminalNodes.add(terminalNode);
        this.contextFreeGrammarRuleIndexes.add(contextFreeGrammarRuleIndex);
    }

    public ContextFreeGrammar getContextFreeGrammar()
    {
        return this.contextFreeGrammar;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof LL1ParseTable))
        {
            return false;
        }

        return this.contextFreeGrammar.equals(((LL1ParseTable)other).getContextFreeGrammar());
    }
}
