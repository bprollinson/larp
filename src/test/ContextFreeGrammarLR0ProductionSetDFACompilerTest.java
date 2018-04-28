import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.grammar.contextfreelanguage.ContextFreeGrammar;
import larp.parser.contextfreelanguage.LR0ProductionSetDFA;
import larp.parser.contextfreelanguage.LR0ProductionSetDFAState;
import larp.parser.regularlanguage.StateTransition;
import larp.parsetree.contextfreelanguage.ConcatenationNode;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.parsetree.contextfreelanguage.DotNode;
import larp.parsetree.contextfreelanguage.EndOfStringNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.ProductionNode;
import larp.parsetree.contextfreelanguage.TerminalNode;
import larp.syntaxcompiler.contextfreelanguage.ContextFreeGrammarLR0ProductionSetDFACompiler;

import java.util.HashSet;
import java.util.Set;

public class ContextFreeGrammarLR0ProductionSetDFACompilerTest
{
    @Test
    public void testCompileCreatesDFAForTerminalRuleFromStartState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Set<ContextFreeGrammarSyntaxNode> productionSet0 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarSyntaxNode> productionSet1 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarSyntaxNode> productionSet2 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarSyntaxNode> productionSet3 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", true, productionSet3);

        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s2));
        s2.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s3));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0);

        ContextFreeGrammar cfg = new ContextFreeGrammar();
        cfg.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(cfg)));
    }

    @Test
    public void testCompileCreatesDFAForNonTerminalRuleFromStartState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Set<ContextFreeGrammarSyntaxNode> productionSet0 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarSyntaxNode> productionSet1 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarSyntaxNode> productionSet2 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarSyntaxNode> productionSet3 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", true, productionSet3);

        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s2));
        s2.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s3));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0);

        ContextFreeGrammar cfg = new ContextFreeGrammar();
        cfg.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(cfg)));
    }

    @Test
    public void testCompileCreateDFAForTerminalAndNonTerminalRuleFromStartState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Set<ContextFreeGrammarSyntaxNode> productionSet0 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarSyntaxNode> productionSet1 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarSyntaxNode> productionSet2 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarSyntaxNode> productionSet3 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarSyntaxNode> productionSet4 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, productionSet4);

        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0);

        ContextFreeGrammar cfg = new ContextFreeGrammar();
        cfg.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        cfg.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(cfg)));
    }

    @Test
    public void testCompileCreateDFAForTerminalRuleFromSubsequentState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Set<ContextFreeGrammarSyntaxNode> productionSet0 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new TerminalNode("b")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarSyntaxNode> productionSet1 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarSyntaxNode> productionSet2 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarSyntaxNode> productionSet3 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarSyntaxNode> productionSet4 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, productionSet3);

        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0);

        ContextFreeGrammar cfg = new ContextFreeGrammar();
        cfg.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(cfg)));
    }

    @Test
    public void testCompileCreateDFAForNonTerminalRuleFromSubsequentState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Set<ContextFreeGrammarSyntaxNode> productionSet0 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new TerminalNode("b")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarSyntaxNode> productionSet1 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarSyntaxNode> productionSet2 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarSyntaxNode> productionSet3 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarSyntaxNode> productionSet4 = new HashSet<ContextFreeGrammarSyntaxNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, productionSet3);

        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarSyntaxNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0);

        ContextFreeGrammar cfg = new ContextFreeGrammar();
        cfg.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(cfg)));
    }

    @Test
    public void testCompileCreateDFAForTerminalAndNonTerminalRuleFromSubsequentState()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileAddsDeepClosureToStartState()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileAddsDeepClosureToSubsequentState()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileLoopsTransitionToExistingState()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileCreatesStateChain()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileAddsMultipleProductionsToNextStateBasedOnSameSymbol()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileRemovesDuplicateProduction()
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
