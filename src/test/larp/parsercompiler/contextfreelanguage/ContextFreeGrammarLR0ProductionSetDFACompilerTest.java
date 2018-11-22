/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.parsercompiler.contextfreelanguage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.automaton.StateTransition;
import larp.grammar.contextfreelanguage.Grammar;
import larp.parser.contextfreelanguage.LR0ProductionSetDFA;
import larp.parser.contextfreelanguage.LR0ProductionSetDFAState;
import larp.parsetree.contextfreelanguage.ConcatenationNode;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarParseTreeNode;
import larp.parsetree.contextfreelanguage.DotNode;
import larp.parsetree.contextfreelanguage.EndOfStringNode;
import larp.parsetree.contextfreelanguage.EpsilonNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.ProductionNode;
import larp.parsetree.contextfreelanguage.TerminalNode;

import java.util.HashSet;
import java.util.Set;

public class ContextFreeGrammarLR0ProductionSetDFACompilerTest
{
    @Test
    public void testCompileCreatesDFAForTerminalRuleFromStartState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", true, productionSet3);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s2));
        s2.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s3));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForNonTerminalRuleFromStartState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", true, productionSet3);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s2));
        s2.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s3));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForTerminalAndNonTerminalRuleFromStartState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, productionSet4);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        grammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForTerminalRuleFromSubsequentState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new TerminalNode("b")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, productionSet4);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForNonTerminalRuleFromSubsequentState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, productionSet4);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForTerminalAndNonTerminalRuleFromSubsequentState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));
        augmentedGrammar.addProduction(new NonTerminalNode("B"), new TerminalNode("b"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new NonTerminalNode("B")));
        productionSet1.add(this.buildProduction(new NonTerminalNode("B"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("B"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, productionSet4);

        Set<ContextFreeGrammarParseTreeNode> productionSet5 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet5.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", true, productionSet5);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("b"), s3));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s4));
        s4.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s5));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));
        grammar.addProduction(new NonTerminalNode("B"), new TerminalNode("b"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileAddsDeepClosureToStartState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new NonTerminalNode("B"));
        augmentedGrammar.addProduction(new NonTerminalNode("B"), new NonTerminalNode("C"));
        augmentedGrammar.addProduction(new NonTerminalNode("C"), new TerminalNode("c"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("A"), new DotNode(), new NonTerminalNode("B")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("B"), new DotNode(), new NonTerminalNode("C")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("C"), new DotNode(), new TerminalNode("c")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("A"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("B"), new NonTerminalNode("C"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("C"), new TerminalNode("c"), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, productionSet4);

        Set<ContextFreeGrammarParseTreeNode> productionSet5 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet5.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", false, productionSet5);

        Set<ContextFreeGrammarParseTreeNode> productionSet6 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet6.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s6 = new LR0ProductionSetDFAState("", true, productionSet6);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("C"), s3));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("c"), s4));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s5));
        s5.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s6));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        grammar.addProduction(new NonTerminalNode("A"), new NonTerminalNode("B"));
        grammar.addProduction(new NonTerminalNode("B"), new NonTerminalNode("C"));
        grammar.addProduction(new NonTerminalNode("C"), new TerminalNode("c"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileAddsDeepClosureToSubsequentState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));
        augmentedGrammar.addProduction(new NonTerminalNode("B"), new NonTerminalNode("C"));
        augmentedGrammar.addProduction(new NonTerminalNode("C"), new NonTerminalNode("D"));
        augmentedGrammar.addProduction(new NonTerminalNode("D"), new TerminalNode("d"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new NonTerminalNode("B")));
        productionSet1.add(this.buildProduction(new NonTerminalNode("B"), new DotNode(), new NonTerminalNode("C")));
        productionSet1.add(this.buildProduction(new NonTerminalNode("C"), new DotNode(), new NonTerminalNode("D")));
        productionSet1.add(this.buildProduction(new NonTerminalNode("D"), new DotNode(), new TerminalNode("d")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("B"), new NonTerminalNode("C"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("C"), new NonTerminalNode("D"), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, productionSet4);

        Set<ContextFreeGrammarParseTreeNode> productionSet5 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet5.add(this.buildProduction(new NonTerminalNode("D"), new TerminalNode("d"), new DotNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", false, productionSet5);

        Set<ContextFreeGrammarParseTreeNode> productionSet6 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet6.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s6 = new LR0ProductionSetDFAState("", false, productionSet6);

        Set<ContextFreeGrammarParseTreeNode> productionSet7 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet7.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s7 = new LR0ProductionSetDFAState("", true, productionSet7);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("C"), s3));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("D"), s4));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("d"), s5));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s6));
        s6.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s7));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));
        grammar.addProduction(new NonTerminalNode("B"), new NonTerminalNode("C"));
        grammar.addProduction(new NonTerminalNode("C"), new NonTerminalNode("D"));
        grammar.addProduction(new NonTerminalNode("D"), new TerminalNode("d"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileLoopsTransitionToExistingState()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new NonTerminalNode("S"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A"), new NonTerminalNode("S")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode(), new NonTerminalNode("S")));
        productionSet3.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A"), new NonTerminalNode("S")));
        productionSet3.add(this.buildProduction(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        productionSet3.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new NonTerminalNode("S"), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, productionSet4);

        Set<ContextFreeGrammarParseTreeNode> productionSet5 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet5.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", false, productionSet5);

        Set<ContextFreeGrammarParseTreeNode> productionSet6 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet6.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s6 = new LR0ProductionSetDFAState("", true, productionSet6);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s4));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s5));
        s5.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s6));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new NonTerminalNode("S"));
        grammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesStateChain()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new TerminalNode("b"), new TerminalNode("c")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new DotNode(), new TerminalNode("c")));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, productionSet4);

        Set<ContextFreeGrammarParseTreeNode> productionSet5 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet5.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", true, productionSet5);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s2.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("c"), s3));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s4));
        s4.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s5));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileAddsMultipleProductionsToNextStateBasedOnSameSymbol()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("b"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A"), new TerminalNode("a")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A"), new TerminalNode("b")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, productionSet4);

        Set<ContextFreeGrammarParseTreeNode> productionSet5 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet5.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", false, productionSet5);

        Set<ContextFreeGrammarParseTreeNode> productionSet6 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet6.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s6 = new LR0ProductionSetDFAState("", true, productionSet6);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s2));
        s2.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s3));
        s2.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("b"), s4));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s5));
        s5.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s6));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("a"));
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("b"));
        grammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileHandlesMultiCharacterTerminalNode()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new TerminalNode("b")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, productionSet4);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("ab"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompilerHandlesEpsilon()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new EpsilonNode());

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new EpsilonNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new EpsilonNode(), new DotNode()));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", true, productionSet2);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s1));
        s1.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s2));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new EpsilonNode());

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompilerHandlesEpsilonWithinStateWithOtherProductions()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new EpsilonNode());

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        productionSet0.add(this.buildProduction(new NonTerminalNode("A"), new DotNode(), new EpsilonNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("A"), new EpsilonNode(), new DotNode()));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, productionSet3);

        Set<ContextFreeGrammarParseTreeNode> productionSet4 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet4.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, productionSet4);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s2));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        grammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));
        grammar.addProduction(new NonTerminalNode("A"), new EpsilonNode());

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileRemovesDuplicateProduction()
    {
        ContextFreeGrammarLR0ProductionSetDFACompiler compiler = new ContextFreeGrammarLR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        Set<ContextFreeGrammarParseTreeNode> productionSet0 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet0.add(this.buildProduction(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        productionSet0.add(this.buildProduction(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, productionSet0);

        Set<ContextFreeGrammarParseTreeNode> productionSet1 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet1.add(this.buildProduction(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, productionSet1);

        Set<ContextFreeGrammarParseTreeNode> productionSet2 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet2.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, productionSet2);

        Set<ContextFreeGrammarParseTreeNode> productionSet3 = new HashSet<ContextFreeGrammarParseTreeNode>();
        productionSet3.add(this.buildProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", true, productionSet3);

        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s2));
        s2.addTransition(new StateTransition<ContextFreeGrammarParseTreeNode, LR0ProductionSetDFAState>(new EndOfStringNode(), s3));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    private ProductionNode buildProduction(NonTerminalNode nonTerminalNode, ContextFreeGrammarParseTreeNode... rightHandNodes)
    {
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(nonTerminalNode);

        ConcatenationNode concatenationNode = new ConcatenationNode();
        for (ContextFreeGrammarParseTreeNode rightHandNode: rightHandNodes)
        {
            concatenationNode.addChild(rightHandNode);
        }
        productionNode.addChild(concatenationNode);

        return productionNode;
    }
}
