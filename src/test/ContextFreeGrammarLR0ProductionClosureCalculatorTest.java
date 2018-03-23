import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.parsetree.contextfreelanguage.ConcatenationNode;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.parsetree.contextfreelanguage.DotNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.ProductionNode;
import larp.parsetree.contextfreelanguage.TerminalNode;
import larp.syntaxcompiler.contextfreelanguage.ContextFreeGrammarLR0ProductionClosureCalculator;

import java.util.HashSet;
import java.util.Set;

public class ContextFreeGrammarLR0ProductionClosureCalculatorTest
{
    @Test
    public void testCalculateClosureDoesNotAddItemsWhenDotNotPresent()
    {
        ContextFreeGrammarLR0ProductionClosureCalculator calculator = new ContextFreeGrammarLR0ProductionClosureCalculator();

        Set<ContextFreeGrammarSyntaxNode> productionSet = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("S")));

        Set<ContextFreeGrammarSyntaxNode> expectedProductionSet = new HashSet<ContextFreeGrammarSyntaxNode>();
        expectedProductionSet.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("S")));

        assertEquals(expectedProductionSet, calculator.calculateClosure(productionSet));
    }

    @Test
    public void testCalculateClosureDoesNotAddItemsWhenDotIsNotBeforeNonTerminal()
    {
        ContextFreeGrammarLR0ProductionClosureCalculator calculator = new ContextFreeGrammarLR0ProductionClosureCalculator();

        Set<ContextFreeGrammarSyntaxNode> productionSet = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new NonTerminalNode("S")));

        Set<ContextFreeGrammarSyntaxNode> expectedProductionSet = new HashSet<ContextFreeGrammarSyntaxNode>();
        expectedProductionSet.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new NonTerminalNode("S")));

        assertEquals(expectedProductionSet, calculator.calculateClosure(productionSet));
    }

    @Test
    public void testCalculateClosureDoesNotAddItemsWhenDotAtEndOfProduction()
    {
        ContextFreeGrammarLR0ProductionClosureCalculator calculator = new ContextFreeGrammarLR0ProductionClosureCalculator();

        Set<ContextFreeGrammarSyntaxNode> productionSet = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("S"), new DotNode()));

        Set<ContextFreeGrammarSyntaxNode> expectedProductionSet = new HashSet<ContextFreeGrammarSyntaxNode>();
        expectedProductionSet.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("S"), new DotNode()));

        assertEquals(expectedProductionSet, calculator.calculateClosure(productionSet));
    }

    @Test
    public void testCalculateClosureAddsSingleProductionWhenDotIsBeforeNonTerminalWithSingleProduction()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCalculateClosureAddsMultipleProductionsWhenDotIsBeforeNonTerminalWithMultipleProductions()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCalculateClosureRemovesDuplicateProduction()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCalculateClosureAddsSingleProductionWhenSetContainsOneRelevantAndOneIrrelevantProduction()
    {
        assertEquals(0, 1);
    }

    private ProductionNode buildProduction(NonTerminalNode nonTerminalNode, ContextFreeGrammarSyntaxNode... rightHandNodes)
    {
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(nonTerminalNode);

        ConcatenationNode concatenationNode = new ConcatenationNode();
        for (ContextFreeGrammarSyntaxNode rightHandNode: rightHandNodes)
        {
            concatenationNode.addChild(rightHandNode);
        }
        productionNode.addChild(concatenationNode);

        return productionNode;
    }
}
