/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.parser.contextfreelanguage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.grammar.contextfreelanguage.ContextFreeGrammar;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarParseTreeNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.TerminalNode;
import larp.util.PairToValueMap;

import java.util.HashSet;

public class LL1ParseTableTest
{
    @Test(expected = LL1ApplyApplyConflictException.class)
    public void testAddCellThrowsExceptionForCellThatAlreadyExists() throws AmbiguousLL1ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable parseTable = new LL1ParseTable(grammar);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);
    }

    @Test
    public void testAddCellDoesNotThrowExceptionForCellThatDoesNotAlreadyExist() throws AmbiguousLL1ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable parseTable = new LL1ParseTable(grammar);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("b"), 0);
    }

    @Test
    public void testCellsEqualReturnsTrue() throws AmbiguousLL1ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        PairToValueMap<NonTerminalNode, ContextFreeGrammarParseTreeNode, Integer> cells = new PairToValueMap<NonTerminalNode, ContextFreeGrammarParseTreeNode, Integer>();
        cells.put(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        LL1ParseTable parseTable = new LL1ParseTable(grammar);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        assertTrue(parseTable.cellsEqual(cells));
    }

    @Test
    public void testCellsEqualReturnsFalse() throws AmbiguousLL1ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        PairToValueMap<NonTerminalNode, ContextFreeGrammarParseTreeNode, Integer> cells = new PairToValueMap<NonTerminalNode, ContextFreeGrammarParseTreeNode, Integer>();
        cells.put(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        LL1ParseTable parseTable = new LL1ParseTable(grammar);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 1);

        assertFalse(parseTable.cellsEqual(cells));
    }

    @Test
    public void testEqualsReturnsTrueForEmptyCFGAndNoTableEntries()
    {
        LL1ParseTable parseTable = new LL1ParseTable(new ContextFreeGrammar());
        LL1ParseTable otherParseTable = new LL1ParseTable(new ContextFreeGrammar());

        assertEquals(otherParseTable, parseTable);
    }

    @Test
    public void testEqualsReturnsTrueForNonEmptyCFGAndTableEntries() throws AmbiguousLL1ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable parseTable = new LL1ParseTable(grammar);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        ContextFreeGrammar otherGrammar = new ContextFreeGrammar();
        otherGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable otherParseTable = new LL1ParseTable(otherGrammar);
        otherParseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        assertEquals(otherParseTable, parseTable);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentCFGs() throws AmbiguousLL1ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable parseTable = new LL1ParseTable(grammar);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        ContextFreeGrammar otherGrammar = new ContextFreeGrammar();
        otherGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        LL1ParseTable otherParseTable = new LL1ParseTable(otherGrammar);
        otherParseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        assertNotEquals(otherParseTable, parseTable);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTableEntries() throws AmbiguousLL1ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable parseTable = new LL1ParseTable(grammar);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        ContextFreeGrammar otherGrammar = new ContextFreeGrammar();
        otherGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable otherParseTable = new LL1ParseTable(otherGrammar);
        otherParseTable.addCell(new NonTerminalNode("S"), new TerminalNode("b"), 0);

        assertNotEquals(otherParseTable, parseTable);
    }

    @Test
    public void testEqualsReturnsTrueForSameTableEntriesInDifferentOrder() throws AmbiguousLL1ParseTableException
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();
        grammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable parseTable = new LL1ParseTable(grammar);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);
        parseTable.addCell(new NonTerminalNode("S"), new TerminalNode("b"), 0);

        ContextFreeGrammar otherGrammar = new ContextFreeGrammar();
        otherGrammar.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LL1ParseTable otherParseTable = new LL1ParseTable(otherGrammar);
        otherParseTable.addCell(new NonTerminalNode("S"), new TerminalNode("b"), 0);
        otherParseTable.addCell(new NonTerminalNode("S"), new TerminalNode("a"), 0);

        assertEquals(otherParseTable, parseTable);
    }

    @Test
    public void testEqualsReturnsFalseForParseTableWithDifferentClass()
    {
        ContextFreeGrammar grammar = new ContextFreeGrammar();

        LL1ParseTable parseTable = new LL1ParseTable(grammar);

        LR0ProductionSetDFAState state = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarParseTreeNode>());

        LR0ParseTable otherParseTable = new LR0ParseTable(grammar, state);

        assertNotEquals(otherParseTable, parseTable);
    }
}
