import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.grammar.contextfreelanguage.ContextFreeGrammar;
import larp.parser.contextfreelanguage.AmbiguousLR0ParseTableException;
import larp.parser.contextfreelanguage.LR0ParseTable;
import larp.parser.contextfreelanguage.LR0ProductionSetDFAState;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.TerminalNode;

import java.util.HashSet;

public class LR0ParseTableTest
{
    @Test(expected = AmbiguousLR0ParseTableException.class)
    public void testAddCellThrowsExceptionForTwoShiftActionsWithTheSameStateAndSymbol()
    {
    }

    @Test
    public void testAddCellThrowsExceptionForTwoShiftActionsWithTheSameStateAndDifferentSymbols()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testAddCellThrowsExceptionForTwoShiftActionsWithDifferentStatesAndTheSameSymbol()
    {
        assertEquals(0, 1);
    }

    @Test(expected = AmbiguousLR0ParseTableException.class)
    public void testAddCellThrowsExceptionForTwoReduceActionsWithTheSameState()
    {
    }

    @Test(expected = AmbiguousLR0ParseTableException.class)
    public void testAddCellThrowsExceptionForShiftActionConflictingWithExistingReduceActionWithSameState()
    {
    }

    @Test(expected = AmbiguousLR0ParseTableException.class)
    public void testAddCellThrowsExceptionForReduceActionConflictingWithExistingShiftActionWithSameState()
    {
    }

    @Test
    public void testEqualsReturnsTrueForEmptyCFGAndNoTableEntries()
    {
        LR0ParseTable parseTable = new LR0ParseTable(new ContextFreeGrammar());
        LR0ParseTable otherParseTable = new LR0ParseTable(new ContextFreeGrammar());

        assertTrue(parseTable.equals(otherParseTable));
    }

    @Test
    public void testEqualsReturnsTrueForNonEmptyCFGAndTableEntries()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentCFGs()
    {
        ContextFreeGrammar cfg = new ContextFreeGrammar();
        cfg.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));

        LR0ProductionSetDFAState state = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

        LR0ParseTable parseTable = new LR0ParseTable(cfg);
        parseTable.addCell(state, new TerminalNode("a"));

        ContextFreeGrammar otherCfg = new ContextFreeGrammar();
        otherCfg.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        LR0ParseTable otherParseTable = new LR0ParseTable(otherCfg);
        otherParseTable.addCell(state, new TerminalNode("a"));

        assertFalse(parseTable.equals(otherParseTable));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentShiftTableEntries()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentReduceTableEntries()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testEqualsReturnsTrueForSameTableEntriesInDifferentOrder()
    {
        assertEquals(0, 1);
    }
}
