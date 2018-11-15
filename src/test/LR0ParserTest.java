/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.grammar.contextfreelanguage.ContextFreeGrammar;
import larp.parser.contextfreelanguage.AmbiguousLR0ParseTableException;
import larp.parser.contextfreelanguage.LL1Parser;
import larp.parser.contextfreelanguage.LL1ParseTable;
import larp.parser.contextfreelanguage.LR0AcceptAction;
import larp.parser.contextfreelanguage.LR0GotoAction;
import larp.parser.contextfreelanguage.LR0Parser;
import larp.parser.contextfreelanguage.LR0ParseTable;
import larp.parser.contextfreelanguage.LR0ProductionSetDFAState;
import larp.parser.contextfreelanguage.LR0ReduceAction;
import larp.parser.contextfreelanguage.LR0ShiftAction;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarParseTreeNode;
import larp.parsetree.contextfreelanguage.EndOfStringNode;
import larp.parsetree.contextfreelanguage.EpsilonNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.TerminalNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LR0ParserTest
{
    @Test
    public void testAcceptsReturnsTrueForCorrectCharacterInSingleCharacterCFG() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state3));
        parseTable.addCell(state2, new TerminalNode("a"), new LR0ReduceAction(1));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state3, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);

        assertTrue(parser.accepts("a"));
    }

    @Test
    public void testAcceptsReturnsFalseForIncorrectCharacterInSingleCharacterCFG() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state3));
        parseTable.addCell(state2, new TerminalNode("a"), new LR0ReduceAction(1));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state3, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);

        assertFalse(parser.accepts("b"));
    }

    @Test
    public void testAcceptsReturnsTrueForMultiCharacterCFGUsingMultipleTerminalNodes() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state4 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state4));
        parseTable.addCell(state2, new TerminalNode("b"), new LR0ShiftAction(state3));
        parseTable.addCell(state3, new TerminalNode("a"), new LR0ReduceAction(1));
        parseTable.addCell(state3, new TerminalNode("b"), new LR0ReduceAction(1));
        parseTable.addCell(state3, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state4, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);

        assertTrue(parser.accepts("ab"));
    }

    @Test
    public void testAcceptsDoesNotEquateCharactersWithMultiCharacterTerminalNode() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("ab"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("ab"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state3));
        parseTable.addCell(state2, new TerminalNode("ab"), new LR0ReduceAction(1));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state3, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);

        assertFalse(parser.accepts("ab"));
    }

    @Test
    public void testAcceptsReturnsFalseForUnmatchedInputCharacter() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state3));
        parseTable.addCell(state2, new TerminalNode("a"), new LR0ReduceAction(1));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state3, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);

        assertFalse(parser.accepts("ab"));
    }

    @Test
    public void testAcceptsReturnsFalseForUnmatchedCFGCharacter() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state4 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state4));
        parseTable.addCell(state2, new TerminalNode("b"), new LR0ShiftAction(state3));
        parseTable.addCell(state3, new TerminalNode("a"), new LR0ReduceAction(1));
        parseTable.addCell(state3, new TerminalNode("b"), new LR0ReduceAction(1));
        parseTable.addCell(state3, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state4, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);

        assertFalse(parser.accepts("a"));
    }

    @Test
    public void testAcceptsReturnsTrueForEmptyString() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new EpsilonNode());

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state2));
        parseTable.addCell(state1, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);

        assertTrue(parser.accepts(""));
    }

    @Test
    public void testAcceptsReturnsFalseForEmptyStringWhenCFGIsEmpty()
    {
        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, null);

        LR0Parser parser = new LR0Parser(parseTable);

        assertFalse(parser.accepts(""));
    }

    @Test
    public void testAcceptsReturnsFalseForNonEmptyStringWhenCFGIsEmpty()
    {
        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, null);

        LR0Parser parser = new LR0Parser(parseTable);

        assertFalse(parser.accepts("a"));
    }

    @Test
    public void testAcceptsReturnsFalseWhenReduceActionRunsOutOfSymbols() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"), new TerminalNode("b"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state2, new TerminalNode("b"), new LR0ReduceAction(1));

        LR0Parser parser = new LR0Parser(parseTable);

        assertFalse(parser.accepts("ab"));
    }

    @Test
    public void testAcceptsReturnsFalseWhenNextStateNotFoundInStack() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("A"), new LR0ReduceAction(1));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0ReduceAction(2));

        LR0Parser parser = new LR0Parser(parseTable);

        assertFalse(parser.accepts("a"));
    }

    @Test
    public void testGetAppliedRulesReturnsEmptyListBeforeParseIsRun() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state4 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new NonTerminalNode("a"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state4));
        parseTable.addCell(state1, new NonTerminalNode("A"), new LR0GotoAction(state3));
        parseTable.addCell(state2, new TerminalNode("a"), new LR0ReduceAction(2));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0ReduceAction(2));
        parseTable.addCell(state3, new TerminalNode("a"), new LR0ReduceAction(1));
        parseTable.addCell(state3, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state4, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);

        List<Integer> expectedRuleIndexes = new ArrayList<Integer>();

        assertEquals(expectedRuleIndexes, parser.getAppliedRules());
    }

    @Test
    public void testGetAppliedRulesReturnsRuleIndexesOnSuccessfulParse() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state4 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new NonTerminalNode("a"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state4));
        parseTable.addCell(state1, new NonTerminalNode("A"), new LR0GotoAction(state3));
        parseTable.addCell(state2, new TerminalNode("a"), new LR0ReduceAction(2));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0ReduceAction(2));
        parseTable.addCell(state3, new TerminalNode("a"), new LR0ReduceAction(1));
        parseTable.addCell(state3, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state4, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);
        parser.accepts("a");

        List<Integer> expectedRuleIndexes = new ArrayList<Integer>();
        expectedRuleIndexes.add(0);
        expectedRuleIndexes.add(1);

        assertEquals(expectedRuleIndexes, parser.getAppliedRules());
    }

    @Test
    public void testGetAppliedRulesReturnsRuleIndexesUntilTableLookupFailure() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state4 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state5 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ProductionSetDFAState state6 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        ContextFreeGrammar augmentedGrammar = new ContextFreeGrammar();
        augmentedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        augmentedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"), new NonTerminalNode("B"));
        augmentedGrammar.addProduction(new NonTerminalNode("A"), new NonTerminalNode("a"));
        augmentedGrammar.addProduction(new NonTerminalNode("B"), new NonTerminalNode("b"));

        LR0ParseTable parseTable = new LR0ParseTable(augmentedGrammar, state1);
        parseTable.addCell(state1, new TerminalNode("a"), new LR0ShiftAction(state2));
        parseTable.addCell(state1, new NonTerminalNode("S"), new LR0GotoAction(state6));
        parseTable.addCell(state1, new NonTerminalNode("A"), new LR0GotoAction(state3));
        parseTable.addCell(state2, new TerminalNode("a"), new LR0ReduceAction(2));
        parseTable.addCell(state2, new TerminalNode("b"), new LR0ReduceAction(2));
        parseTable.addCell(state2, new EndOfStringNode(), new LR0ReduceAction(2));
        parseTable.addCell(state3, new TerminalNode("b"), new LR0ShiftAction(state5));
        parseTable.addCell(state3, new NonTerminalNode("B"), new LR0GotoAction(state4));
        parseTable.addCell(state4, new TerminalNode("a"), new LR0ReduceAction(1));
        parseTable.addCell(state4, new TerminalNode("b"), new LR0ReduceAction(1));
        parseTable.addCell(state4, new EndOfStringNode(), new LR0ReduceAction(1));
        parseTable.addCell(state5, new TerminalNode("a"), new LR0ReduceAction(3));
        parseTable.addCell(state5, new TerminalNode("b"), new LR0ReduceAction(3));
        parseTable.addCell(state5, new EndOfStringNode(), new LR0ReduceAction(3));
        parseTable.addCell(state6, new EndOfStringNode(), new LR0AcceptAction());

        LR0Parser parser = new LR0Parser(parseTable);
        parser.accepts("a");

        List<Integer> expectedRuleIndexes = new ArrayList<Integer>();
        expectedRuleIndexes.add(1);

        assertEquals(expectedRuleIndexes, parser.getAppliedRules());
    }

    @Test
    public void testStructureEqualsReturnsTrueWhenParseTablesHaveSameStructure() throws AmbiguousLR0ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        LR0ProductionSetDFAState startState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ParseTable parseTable = new LR0ParseTable(grammar, startState);
        parseTable.addCell(startState, new TerminalNode("a"), new LR0ReduceAction(0));
        parseTable.addCell(startState, new TerminalNode("b"), new LR0ReduceAction(1));
        LR0Parser parser = new LR0Parser(parseTable);

        LR0ProductionSetDFAState otherStartState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ParseTable otherParseTable = new LR0ParseTable(grammar, otherStartState);
        otherParseTable.addCell(otherStartState, new TerminalNode("a"), new LR0ReduceAction(0));
        otherParseTable.addCell(otherStartState, new TerminalNode("b"), new LR0ReduceAction(1));
        LR0Parser otherParser = new LR0Parser(otherParseTable);

        assertTrue(parser.structureEquals(otherParser));
    }

    @Test
    public void testStructureEqualsReturnsFalseWhenParseTablesHaveDifferentStructure() throws AmbiguousLR0ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        LR0ProductionSetDFAState startState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ParseTable parseTable = new LR0ParseTable(grammar, startState);
        parseTable.addCell(startState, new TerminalNode("a"), new LR0ReduceAction(0));
        parseTable.addCell(startState, new TerminalNode("b"), new LR0ReduceAction(1));
        LR0Parser parser = new LR0Parser(parseTable);

        LR0ProductionSetDFAState otherStartState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ParseTable otherParseTable = new LR0ParseTable(grammar, otherStartState);
        otherParseTable.addCell(otherStartState, new TerminalNode("a"), new LR0ReduceAction(0));
        otherParseTable.addCell(otherStartState, new TerminalNode("b"), new LR0ReduceAction(0));
        LR0Parser otherParser = new LR0Parser(otherParseTable);

        assertFalse(parser.structureEquals(otherParser));
    }

    @Test
    public void testStructureEqualsReturnsFalseForParserWithDifferentClass()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();

        LR0ProductionSetDFAState startState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());
        LR0ParseTable parseTable = new LR0ParseTable(grammar, startState);
        LR0Parser parser = new LR0Parser(parseTable);

        LL1ParseTable otherParseTable = new LL1ParseTable(grammar);
        LL1Parser otherParser = new LL1Parser(otherParseTable);

        assertFalse(parser.structureEquals(otherParser));
    }
}
