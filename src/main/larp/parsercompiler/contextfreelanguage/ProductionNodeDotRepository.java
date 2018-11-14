/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.parsercompiler.contextfreelanguage;

import larp.parsetree.contextfreelanguage.ConcatenationNode;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.parsetree.contextfreelanguage.DotNode;
import larp.parsetree.contextfreelanguage.ProductionNode;

import java.util.List;

public class ProductionNodeDotRepository
{
    public ContextFreeGrammarSyntaxNode findProductionSymbolAfterDot(ContextFreeGrammarSyntaxNode productionNode)
    {
        List<ContextFreeGrammarSyntaxNode> childNodes = productionNode.getChildNodes().get(1).getChildNodes();

        boolean lastNodeWasDot = false;
        for (ContextFreeGrammarSyntaxNode childNode: childNodes)
        {
            if (lastNodeWasDot)
            {
                return childNode;
            }

            lastNodeWasDot = childNode instanceof DotNode;
        }

        return null;
    }

    public ProductionNode addDotToProductionRightHandSide(ContextFreeGrammarSyntaxNode productionNode)
    {
        ProductionNode newProductionNode = new ProductionNode();
        newProductionNode.addChild(productionNode.getChildNodes().get(0));

        List<ContextFreeGrammarSyntaxNode> childNodes = productionNode.getChildNodes().get(1).getChildNodes();
        ConcatenationNode newConcatenationNode = new ConcatenationNode();
        newConcatenationNode.addChild(new DotNode());
        for (ContextFreeGrammarSyntaxNode childNode: childNodes)
        {
            newConcatenationNode.addChild(childNode);
        }
        newProductionNode.addChild(newConcatenationNode);

        return newProductionNode;
    }

    public ContextFreeGrammarSyntaxNode shiftDotInProduction(ContextFreeGrammarSyntaxNode productionNode)
    {
        ProductionNode newProductionNode = new ProductionNode();
        newProductionNode.addChild(productionNode.getChildNodes().get(0));

        List<ContextFreeGrammarSyntaxNode> childNodes = productionNode.getChildNodes().get(1).getChildNodes();
        ConcatenationNode newConcatenationNode = new ConcatenationNode();
        boolean lastNodeWasDot = false;
        for (ContextFreeGrammarSyntaxNode childNode: childNodes)
        {
            boolean currentNodeIsDot = childNode instanceof DotNode;
            if (currentNodeIsDot)
            {
                lastNodeWasDot = true;
                continue;
            }
            newConcatenationNode.addChild(childNode);
            if (lastNodeWasDot)
            {
                newConcatenationNode.addChild(new DotNode());
            }
            lastNodeWasDot = false;
        }
        newProductionNode.addChild(newConcatenationNode);

        return newProductionNode;
    }
}
