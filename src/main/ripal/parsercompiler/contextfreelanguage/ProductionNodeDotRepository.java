/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package ripal.parsercompiler.contextfreelanguage;

import ripal.parsetree.contextfreelanguage.ConcatenationNode;
import ripal.parsetree.contextfreelanguage.DotNode;
import ripal.parsetree.contextfreelanguage.Node;
import ripal.parsetree.contextfreelanguage.ProductionNode;

import java.util.List;

public class ProductionNodeDotRepository
{
    public Node findProductionSymbolAfterDot(Node productionNode)
    {
        List<Node> childNodes = productionNode.getChildNodes().get(1).getChildNodes();

        boolean lastNodeWasDot = false;
        for (Node childNode: childNodes)
        {
            if (lastNodeWasDot)
            {
                return childNode;
            }

            lastNodeWasDot = childNode instanceof DotNode;
        }

        return null;
    }

    public List<Node> findProductionSymbolsAfterDot(Node productionNode)
    {
        List<Node> childNodes = productionNode.getChildNodes().get(1).getChildNodes();
        int dotPosition = childNodes.indexOf(new DotNode());

        if (dotPosition == -1)
        {
            return null;
        }

        return childNodes.subList(dotPosition + 1, childNodes.size());
    }

    public ProductionNode addDotToProduction(Node productionNode)
    {
        ProductionNode newProductionNode = new ProductionNode();
        newProductionNode.addChild(productionNode.getChildNodes().get(0));

        List<Node> childNodes = productionNode.getChildNodes().get(1).getChildNodes();
        ConcatenationNode newConcatenationNode = new ConcatenationNode();
        newConcatenationNode.addChild(new DotNode());
        for (Node childNode: childNodes)
        {
            newConcatenationNode.addChild(childNode);
        }
        newProductionNode.addChild(newConcatenationNode);

        return newProductionNode;
    }

    public Node shiftDotInProduction(Node productionNode)
    {
        ProductionNode newProductionNode = new ProductionNode();
        newProductionNode.addChild(productionNode.getChildNodes().get(0));

        List<Node> childNodes = productionNode.getChildNodes().get(1).getChildNodes();
        ConcatenationNode newConcatenationNode = new ConcatenationNode();
        boolean lastNodeWasDot = false;
        for (Node childNode: childNodes)
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

    public Node removeDotFromProduction(Node production)
    {
        ProductionNode newProduction = new ProductionNode();
        newProduction.addChild(production.getChildNodes().get(0));

        ConcatenationNode newConcatenationNode = new ConcatenationNode();
        Node concatenationNode = production.getChildNodes().get(1);
        for (int i = 0; i < concatenationNode.getChildNodes().size(); i++)
        {
            Node childNode = concatenationNode.getChildNodes().get(i);
            if (!(childNode instanceof DotNode))
            {
                newConcatenationNode.addChild(childNode);
            }
        }
        newProduction.addChild(newConcatenationNode);

        return newProduction;
    }
}
