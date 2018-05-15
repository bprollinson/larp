package larp.syntaxcompiler.contextfreelanguage;

import larp.grammar.contextfreelanguage.ContextFreeGrammar;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.parsetree.contextfreelanguage.EpsilonNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.TerminalNode;
import larp.util.SetMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FirstSetCalculator
{
    private ContextFreeGrammar grammar;
    private SetMap<ContextFreeGrammarSyntaxNode, Integer> nonTerminalRules;

    public FirstSetCalculator(ContextFreeGrammar grammar)
    {
        this.grammar = grammar;
        this.nonTerminalRules = new SetMap<ContextFreeGrammarSyntaxNode, Integer>();

        List<ContextFreeGrammarSyntaxNode> productions = this.grammar.getProductions();
        for (int i = 0; i < productions.size(); i++)
        {
            ContextFreeGrammarSyntaxNode productionNode = productions.get(i);
            this.nonTerminalRules.put(productionNode.getChildNodes().get(0), i);
        }
    }

    public Set<ContextFreeGrammarSyntaxNode> getFirst(int ruleIndex)
    {
        Set<Integer> rulesUsed = new HashSet<Integer>();

        return this.getFirstRecursive(ruleIndex, rulesUsed);
    }

    public Set<ContextFreeGrammarSyntaxNode> getFirst(NonTerminalNode node)
    {
        Set<ContextFreeGrammarSyntaxNode> results = new HashSet<ContextFreeGrammarSyntaxNode>();
        Set<Integer> existingSet = this.nonTerminalRules.get(node);
        if (existingSet != null)
        {
            for (int index: existingSet)
            {
                results.addAll(this.getFirst(index));
            }
        }

        return results;
    }

    private Set<ContextFreeGrammarSyntaxNode> getFirstRecursive(int ruleIndex, Set<Integer> rulesUsed)
    {
        rulesUsed.add(ruleIndex);

        ContextFreeGrammarSyntaxNode concatenationNode = grammar.getProduction(ruleIndex).getChildNodes().get(1);
        if (concatenationNode.getChildNodes().get(0) instanceof TerminalNode)
        {
            return this.getFirstFromTerminalNode(concatenationNode.getChildNodes().get(0));
        }
        else if (concatenationNode.getChildNodes().get(0) instanceof NonTerminalNode)
        {
            return this.getFirstFromNonterminalNode(concatenationNode, rulesUsed);
        }
        else if (concatenationNode.getChildNodes().get(0) instanceof EpsilonNode)
        {
            return this.getFirstFromEpsilon();
        }

        return null;
    }

    private Set<ContextFreeGrammarSyntaxNode> getFirstFromTerminalNode(ContextFreeGrammarSyntaxNode terminalNode)
    {
        Set<ContextFreeGrammarSyntaxNode> results = new HashSet<ContextFreeGrammarSyntaxNode>();
        String value = ((TerminalNode)terminalNode).getValue();
        results.add(new TerminalNode(value.substring(0, 1)));

        return results;
    }

    private Set<ContextFreeGrammarSyntaxNode> getFirstFromNonterminalNode(ContextFreeGrammarSyntaxNode concatenationNode, Set<Integer> rulesUsed)
    {
        Set<ContextFreeGrammarSyntaxNode> results = new HashSet<ContextFreeGrammarSyntaxNode>();

        int index = 0;
        boolean epsilonFound = true;
        while (index < concatenationNode.getChildNodes().size() && epsilonFound)
        {
            epsilonFound = false;

            if (concatenationNode.getChildNodes().get(index) instanceof TerminalNode)
            {
                results.add(concatenationNode.getChildNodes().get(index));
                break;
            }

            Set<Integer> childRuleIndices = this.nonTerminalRules.get(concatenationNode.getChildNodes().get(index));
            if (childRuleIndices != null)
            {
                for (Integer childRuleIndex: childRuleIndices)
                {
                    if (childRuleIndex != null && !rulesUsed.contains(childRuleIndex))
                    {
                        Set<ContextFreeGrammarSyntaxNode> childSet = this.getFirstRecursive(childRuleIndex, rulesUsed);
                        epsilonFound = childSet.contains(new EpsilonNode());
                        childSet.remove(new EpsilonNode());
                        results.addAll(childSet);
                    }
                }
            } else {
                epsilonFound = false;
            }

            index++;
        }

        if (epsilonFound)
        {
            results.add(new EpsilonNode());
        }

        return results;
    }

    private Set<ContextFreeGrammarSyntaxNode> getFirstFromEpsilon()
    {
        Set<ContextFreeGrammarSyntaxNode> results = new HashSet<ContextFreeGrammarSyntaxNode>();
        results.add(new EpsilonNode());

        return results;
    }
}
