import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.automaton.contextfreelanguage.FirstSetCalculator;
import larp.grammar.contextfreelanguage.ConcatenationNode;
import larp.grammar.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.grammar.contextfreelanguage.EpsilonNode;
import larp.grammar.contextfreelanguage.NonTerminalNode;
import larp.grammar.contextfreelanguage.ProductionNode;
import larp.grammar.contextfreelanguage.TerminalNode;
import larp.parsetable.ContextFreeGrammar;

import java.util.HashSet;

public class FirstSetCalculatorTest
{
    @Test
    public void testGetFirstReturnsSingleTerminalDirectlyFromProduction()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("a"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("a"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstIgnoresSubsequentCharacter()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("a"));
        concatenationNode.addChild(new TerminalNode("b"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("a"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsSingleTerminalWithIntermediateNonTerminalProduction()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("a"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("a"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsMultipleTerminalsWithIntermediateNonTerminalProductions()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("a"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("b"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("a"));
        expectedFirsts.add(new TerminalNode("b"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstDetectsCycle()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("S"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("a"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("a"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstIgnoresNonTerminalWithoutProductions()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsEpsilonForEpsilonTransition()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<ContextFreeGrammarSyntaxNode> expectedFirsts = new HashSet<ContextFreeGrammarSyntaxNode>();
        expectedFirsts.add(new EpsilonNode());
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsSubsequentTerminalFromAfterEpsilonNonTerminal()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        concatenationNode.addChild(new TerminalNode("b"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("b"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsMultipleTerminalsWhenFirstNonTerminalGoesToTerminalOrEpsilon()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        concatenationNode.addChild(new TerminalNode("b"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("a"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("a"));
        expectedFirsts.add(new TerminalNode("b"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsEpsilonWhenAllNonTerminalsGoToEpsilon()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        concatenationNode.addChild(new NonTerminalNode("B"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("B"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<ContextFreeGrammarSyntaxNode> expectedFirsts = new HashSet<ContextFreeGrammarSyntaxNode>();
        expectedFirsts.add(new EpsilonNode());
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsTerminalAfterSeriesOfEpsilonNonTerminals()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        concatenationNode.addChild(new NonTerminalNode("B"));
        concatenationNode.addChild(new TerminalNode("c"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("B"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<ContextFreeGrammarSyntaxNode> expectedFirsts = new HashSet<ContextFreeGrammarSyntaxNode>();
        expectedFirsts.add(new TerminalNode("c"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsNonterminalFirstAfterSeriesOfEpsilonNonTerminals()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        concatenationNode.addChild(new NonTerminalNode("B"));
        concatenationNode.addChild(new NonTerminalNode("C"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("B"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("C"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("c"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<ContextFreeGrammarSyntaxNode> expectedFirsts = new HashSet<ContextFreeGrammarSyntaxNode>();
        expectedFirsts.add(new TerminalNode("c"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstIgnoresSubsequentCharacterAfterFirstTerminalrAfterEpsilon()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        concatenationNode.addChild(new TerminalNode("b"));
        concatenationNode.addChild(new TerminalNode("c"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("b"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstIgnoresSubsequentCharacterAfterFirstNonEpsilonNoTerminalAfterEpsilon()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new NonTerminalNode("A"));
        concatenationNode.addChild(new NonTerminalNode("B"));
        concatenationNode.addChild(new NonTerminalNode("C"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("A"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new EpsilonNode());
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("B"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("b"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("C"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("c"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("b"));
        assertEquals(expectedFirsts, calculator.getFirst(0));
    }

    @Test
    public void testGetFirstReturnsMultipleTerminalNodesForNonTerminalNode()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        ConcatenationNode concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("a"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        productionNode = new ProductionNode();
        productionNode.addChild(new NonTerminalNode("S"));
        concatenationNode = new ConcatenationNode();
        concatenationNode.addChild(new TerminalNode("b"));
        productionNode.addChild(concatenationNode);
        grammar.addProduction(productionNode);

        FirstSetCalculator calculator = new FirstSetCalculator(grammar);
        HashSet<TerminalNode> expectedFirsts = new HashSet<TerminalNode>();
        expectedFirsts.add(new TerminalNode("a"));
        expectedFirsts.add(new TerminalNode("b"));
        assertEquals(expectedFirsts, calculator.getFirst(new NonTerminalNode("S")));
    }
}
