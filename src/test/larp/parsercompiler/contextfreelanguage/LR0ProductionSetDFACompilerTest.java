/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.parsercompiler.contextfreelanguage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.automaton.StateTransition;
import larp.grammar.contextfreelanguage.Grammar;
import larp.parser.contextfreelanguage.LR0ProductionSetDFA;
import larp.parser.contextfreelanguage.LR0ProductionSetDFAState;
import larp.parsetree.contextfreelanguage.ConcatenationNode;
import larp.parsetree.contextfreelanguage.DotNode;
import larp.parsetree.contextfreelanguage.EndOfStringNode;
import larp.parsetree.contextfreelanguage.EpsilonNode;
import larp.parsetree.contextfreelanguage.Node;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.ProductionNode;
import larp.parsetree.contextfreelanguage.TerminalNode;

import java.util.HashSet;
import java.util.Set;

public class LR0ProductionSetDFACompilerTest
{
    @Test
    public void testCompileReturnsNullForEmptyGrammar()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar grammar = new Grammar();

        assertNull(compiler.compile(grammar));
    }

    @Test
    public void testCompileCreatesDFAForTerminalRuleFromStartState()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", true, closureRule3);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s2));
        s2.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s3));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForNonTerminalRuleFromStartState()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", true, closureRule3);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s1));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s2));
        s2.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s3));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForTerminalAndNonTerminalRuleFromStartState()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, closureRule4);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s2));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        grammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForTerminalRuleFromSubsequentState()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new TerminalNode("b")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, closureRule4);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForNonTerminalRuleFromSubsequentState()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, closureRule4);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileCreatesDFAForTerminalAndNonTerminalRuleFromSubsequentState()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));
        augmentedGrammar.addProduction(new NonTerminalNode("B"), new TerminalNode("b"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new NonTerminalNode("B")));
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("B"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("B"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, closureRule4);

        Set<GrammarClosureRule> closureRule5 = new HashSet<GrammarClosureRule>();
        closureRule5.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", true, closureRule5);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("b"), s3));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s4));
        s4.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s5));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));
        grammar.addProduction(new NonTerminalNode("B"), new TerminalNode("b"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileAddsDeepClosureToStartState()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new NonTerminalNode("B"));
        augmentedGrammar.addProduction(new NonTerminalNode("B"), new NonTerminalNode("C"));
        augmentedGrammar.addProduction(new NonTerminalNode("C"), new TerminalNode("c"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("A"), new DotNode(), new NonTerminalNode("B")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("B"), new DotNode(), new NonTerminalNode("C")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("C"), new DotNode(), new TerminalNode("c")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("A"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("B"), new NonTerminalNode("C"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("C"), new TerminalNode("c"), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, closureRule4);

        Set<GrammarClosureRule> closureRule5 = new HashSet<GrammarClosureRule>();
        closureRule5.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", false, closureRule5);

        Set<GrammarClosureRule> closureRule6 = new HashSet<GrammarClosureRule>();
        closureRule6.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s6 = new LR0ProductionSetDFAState("", true, closureRule6);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s1));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("C"), s3));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("c"), s4));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s5));
        s5.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s6));
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
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"));
        augmentedGrammar.addProduction(new NonTerminalNode("B"), new NonTerminalNode("C"));
        augmentedGrammar.addProduction(new NonTerminalNode("C"), new NonTerminalNode("D"));
        augmentedGrammar.addProduction(new NonTerminalNode("D"), new TerminalNode("d"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new NonTerminalNode("B")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new NonTerminalNode("B")));
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("B"), new DotNode(), new NonTerminalNode("C")));
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("C"), new DotNode(), new NonTerminalNode("D")));
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("D"), new DotNode(), new TerminalNode("d")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new NonTerminalNode("B"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("B"), new NonTerminalNode("C"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("C"), new NonTerminalNode("D"), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, closureRule4);

        Set<GrammarClosureRule> closureRule5 = new HashSet<GrammarClosureRule>();
        closureRule5.add(this.buildClosureRule(new NonTerminalNode("D"), new TerminalNode("d"), new DotNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", false, closureRule5);

        Set<GrammarClosureRule> closureRule6 = new HashSet<GrammarClosureRule>();
        closureRule6.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s6 = new LR0ProductionSetDFAState("", false, closureRule6);

        Set<GrammarClosureRule> closureRule7 = new HashSet<GrammarClosureRule>();
        closureRule7.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s7 = new LR0ProductionSetDFAState("", true, closureRule7);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("B"), s2));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("C"), s3));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("D"), s4));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("d"), s5));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s6));
        s6.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s7));
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
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new NonTerminalNode("S"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A"), new NonTerminalNode("S")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode(), new NonTerminalNode("S")));
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A"), new NonTerminalNode("S")));
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new NonTerminalNode("S"), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, closureRule4);

        Set<GrammarClosureRule> closureRule5 = new HashSet<GrammarClosureRule>();
        closureRule5.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", false, closureRule5);

        Set<GrammarClosureRule> closureRule6 = new HashSet<GrammarClosureRule>();
        closureRule6.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s6 = new LR0ProductionSetDFAState("", true, closureRule6);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s3));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s3));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s4));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s5));
        s5.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s6));
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
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new TerminalNode("b"), new TerminalNode("c")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new DotNode(), new TerminalNode("c")));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, closureRule4);

        Set<GrammarClosureRule> closureRule5 = new HashSet<GrammarClosureRule>();
        closureRule5.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", true, closureRule5);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s2.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("c"), s3));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s4));
        s4.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s5));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileAddsMultipleProductionsToNextStateBasedOnSameSymbol()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("b"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A"), new TerminalNode("a")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A"), new TerminalNode("b")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", false, closureRule4);

        Set<GrammarClosureRule> closureRule5 = new HashSet<GrammarClosureRule>();
        closureRule5.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s5 = new LR0ProductionSetDFAState("", false, closureRule5);

        Set<GrammarClosureRule> closureRule6 = new HashSet<GrammarClosureRule>();
        closureRule6.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s6 = new LR0ProductionSetDFAState("", true, closureRule6);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s2));
        s2.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s3));
        s2.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("b"), s4));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s5));
        s5.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s6));
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
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a"), new TerminalNode("b")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode(), new TerminalNode("b")));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, closureRule4);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("b"), s2));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("ab"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompilerHandlesEpsilon()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new EpsilonNode());

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new EpsilonNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new EpsilonNode(), new DotNode()));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", true, closureRule2);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s1));
        s1.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s2));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new EpsilonNode());

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompilerHandlesEpsilonWithinStateWithOtherClosureRules()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new EpsilonNode());

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new NonTerminalNode("A")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("A"), new DotNode(), new TerminalNode("a")));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("A"), new DotNode(), new EpsilonNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("A"), new EpsilonNode(), new DotNode()));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("A"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S"), new NonTerminalNode("A"), new DotNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", false, closureRule3);

        Set<GrammarClosureRule> closureRule4 = new HashSet<GrammarClosureRule>();
        closureRule4.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s4 = new LR0ProductionSetDFAState("", true, closureRule4);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("A"), s2));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s3));
        s3.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s4));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        grammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));
        grammar.addProduction(new NonTerminalNode("A"), new EpsilonNode());

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    @Test
    public void testCompileRemovesDuplicateClosureRule()
    {
        LR0ProductionSetDFACompiler compiler = new LR0ProductionSetDFACompiler();

        Grammar augmentedGrammar = new Grammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        Set<GrammarClosureRule> closureRule0 = new HashSet<GrammarClosureRule>();
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S'"), new DotNode(), new NonTerminalNode("S"), new EndOfStringNode()));
        closureRule0.add(this.buildClosureRule(new NonTerminalNode("S"), new DotNode(), new TerminalNode("a")));
        LR0ProductionSetDFAState s0 = new LR0ProductionSetDFAState("", false, closureRule0);

        Set<GrammarClosureRule> closureRule1 = new HashSet<GrammarClosureRule>();
        closureRule1.add(this.buildClosureRule(new NonTerminalNode("S"), new TerminalNode("a"), new DotNode()));
        LR0ProductionSetDFAState s1 = new LR0ProductionSetDFAState("", false, closureRule1);

        Set<GrammarClosureRule> closureRule2 = new HashSet<GrammarClosureRule>();
        closureRule2.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new DotNode(), new EndOfStringNode()));
        LR0ProductionSetDFAState s2 = new LR0ProductionSetDFAState("", false, closureRule2);

        Set<GrammarClosureRule> closureRule3 = new HashSet<GrammarClosureRule>();
        closureRule3.add(this.buildClosureRule(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode(), new DotNode()));
        LR0ProductionSetDFAState s3 = new LR0ProductionSetDFAState("", true, closureRule3);

        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new TerminalNode("a"), s1));
        s0.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new NonTerminalNode("S"), s2));
        s2.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(new EndOfStringNode(), s3));
        LR0ProductionSetDFA expectedProductionSetDFA = new LR0ProductionSetDFA(s0, augmentedGrammar);

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        assertTrue(expectedProductionSetDFA.structureEquals(compiler.compile(grammar)));
    }

    private GrammarClosureRule buildClosureRule(NonTerminalNode nonTerminalNode, Node... rightHandNodes)
    {
        ProductionNode productionNode = new ProductionNode();
        productionNode.addChild(nonTerminalNode);

        ConcatenationNode concatenationNode = new ConcatenationNode();
        for (Node rightHandNode: rightHandNodes)
        {
            concatenationNode.addChild(rightHandNode);
        }
        productionNode.addChild(concatenationNode);

        return new GrammarClosureRule(productionNode);
    }
}
