/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.grammar.contextfreelanguage;

import larp.parsetree.contextfreelanguage.ConcatenationNode;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarParseTreeNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.ProductionNode;

import java.util.ArrayList;
import java.util.List;

public class ContextFreeGrammar
{
    private List<ContextFreeGrammarParseTreeNode> productions;

    public ContextFreeGrammar()
    {
        this.productions = new ArrayList<ContextFreeGrammarParseTreeNode>();
    }

    public void addProduction(ContextFreeGrammarParseTreeNode productionNode)
    {
        this.productions.add(productionNode);
    }

    public void addProduction(NonTerminalNode nonTerminalNode, ContextFreeGrammarParseTreeNode... rightHandNodes)
    {
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(nonTerminalNode);

        ConcatenationNode concatenationNode = new ConcatenationNode();
        for (ContextFreeGrammarParseTreeNode rightHandNode: rightHandNodes)
        {
            concatenationNode.addChild(rightHandNode);
        }
        productionNode.addChild(concatenationNode);

        this.addProduction(productionNode);
    }

    public ContextFreeGrammarParseTreeNode getProduction(int index)
    {
        return this.productions.get(index);
    }

    public List<ContextFreeGrammarParseTreeNode> getProductions()
    {
        return this.productions;
    }

    public NonTerminalNode getStartSymbol()
    {
        if (this.productions.size() == 0)
        {
            return null;
        }

        return (NonTerminalNode)this.productions.get(0).getChildNodes().get(0);
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ContextFreeGrammar))
        {
            return false;
        }

        return this.productions.equals(((ContextFreeGrammar)other).getProductions());
    }
}
