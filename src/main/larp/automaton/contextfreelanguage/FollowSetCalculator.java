package larp.automaton.contextfreelanguage;

import larp.grammar.contextfreelanguage.ConcatenationNode;
import larp.grammar.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.grammar.contextfreelanguage.EndOfStringNode;
import larp.grammar.contextfreelanguage.EpsilonNode;
import larp.grammar.contextfreelanguage.NonTerminalNode;
import larp.grammar.contextfreelanguage.TerminalNode;
import larp.parsetable.ContextFreeGrammar;
import larp.util.SetMap;

import java.util.HashSet;
import java.util.Vector;

public class FollowSetCalculator
{
    private ContextFreeGrammar grammar;
    private SetMap<ContextFreeGrammarSyntaxNode, ContextFreeGrammarSyntaxNode> follows;
    private FirstSetCalculator firstSetCalculator;

    public FollowSetCalculator(ContextFreeGrammar grammar)
    {
        this.grammar = grammar;
        this.follows = new SetMap<ContextFreeGrammarSyntaxNode, ContextFreeGrammarSyntaxNode>();
        this.firstSetCalculator = new FirstSetCalculator(this.grammar);

        this.addStartSymbolDefaultNode();
        this.addFollowNodes();
        this.propagateFollowNodes();
    }

    public HashSet<ContextFreeGrammarSyntaxNode> getFollow(NonTerminalNode nonTerminal)
    {
        return this.follows.get(nonTerminal);
    }

    private void addStartSymbolDefaultNode()
    {
        this.follows.put(this.grammar.getStartSymbol(), new EndOfStringNode());
    }

    private void addFollowNodes()
    {
        HashSet<NonTerminalNode> nullableNonTerminals = new HashSet<NonTerminalNode>();
        Vector<ContextFreeGrammarSyntaxNode> productions = this.grammar.getProductions();
        for (int i = 0; i < productions.size(); i++)
        {
            ContextFreeGrammarSyntaxNode production = productions.get(i);
            ContextFreeGrammarSyntaxNode rightHandSide = production.getChildNodes().get(1);
            if (rightHandSide.getChildNodes().size() == 1 && rightHandSide.getChildNodes().get(0) instanceof EpsilonNode)
            {
                nullableNonTerminals.add((NonTerminalNode)production.getChildNodes().get(0));
            }
        }

        for (int i = 0; i < productions.size(); i++)
        {
            ContextFreeGrammarSyntaxNode production = productions.get(i);
            this.addFollowNodesForProduction(production);
        }
    }

    private void addFollowNodesForProduction(ContextFreeGrammarSyntaxNode production)
    {
        ContextFreeGrammarSyntaxNode rightHandSide = production.getChildNodes().get(1);

        for (int i = 0; i < rightHandSide.getChildNodes().size() - 1; i++)
        {
            ContextFreeGrammarSyntaxNode previousNode = rightHandSide.getChildNodes().get(i);

            boolean done = false;
            int j = i + 1;
            while (j < rightHandSide.getChildNodes().size() && !done)
            {
                done = true;

                ContextFreeGrammarSyntaxNode node = rightHandSide.getChildNodes().get(j);

                if (previousNode instanceof NonTerminalNode && node instanceof TerminalNode)
                {
                    this.follows.put(previousNode, new TerminalNode(((TerminalNode)node).getValue().substring(0, 1)));
                }
                if (previousNode instanceof NonTerminalNode && node instanceof NonTerminalNode)
                {
                    HashSet<ContextFreeGrammarSyntaxNode> firstNodes = this.firstSetCalculator.getFirst((NonTerminalNode)node);
                    if (firstNodes.contains(new EpsilonNode()))
                    {
                        done = false;
                        firstNodes.remove(new EpsilonNode());
                    }
                    for (ContextFreeGrammarSyntaxNode firstNode: firstNodes)
                    {
                        this.follows.put(previousNode, firstNode);
                    }
                }

                j++;
            }
        }
    }

    private void propagateFollowNodes()
    {
        Vector<ContextFreeGrammarSyntaxNode> productions = this.grammar.getProductions();
        boolean done = false;

        while (!done)
        {
            for (int i = 0; i < productions.size(); i++)
            {
                ContextFreeGrammarSyntaxNode production = productions.get(i);
                done = this.propagateFollowNodesForProduction(production);
            }
        }
    }

    private boolean propagateFollowNodesForProduction(ContextFreeGrammarSyntaxNode production)
    {
        boolean done = true;

        NonTerminalNode leftHandSide = (NonTerminalNode)production.getChildNodes().get(0);
        HashSet<ContextFreeGrammarSyntaxNode> leftHandFollow = this.follows.get(leftHandSide);

        if (leftHandFollow != null)
        {
            ContextFreeGrammarSyntaxNode rightHandSide = production.getChildNodes().get(1);

            boolean innerDone = false;
            int i = rightHandSide.getChildNodes().size() - 1;

            while (i >= 0 && !innerDone)
            {
                innerDone = true;

                ContextFreeGrammarSyntaxNode lastNode = rightHandSide.getChildNodes().get(i);
                if (lastNode instanceof NonTerminalNode)
                {
                    for (ContextFreeGrammarSyntaxNode parentFollow : leftHandFollow)
                    {
                        int sizeBefore = 0;
                        HashSet<ContextFreeGrammarSyntaxNode> currentFollows = this.follows.get(lastNode);
                        if (currentFollows != null)
                        {
                             sizeBefore = currentFollows.size();
                        }

                        this.follows.put(lastNode, parentFollow);
                        int sizeAfter = this.follows.get(lastNode).size();

                        if (sizeBefore != sizeAfter)
                        {
                            done = false;
                        }
                    }

                    HashSet<ContextFreeGrammarSyntaxNode> firsts = this.firstSetCalculator.getFirst((NonTerminalNode)lastNode);
                    if (firsts.contains(new EpsilonNode()))
                    {
                        innerDone = false;
                    }
                }

                i--;
            }
        }

        return done;
    }
}
