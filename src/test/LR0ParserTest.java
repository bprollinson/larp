import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.grammar.contextfreelanguage.ContextFreeGrammar;
import larp.parser.contextfreelanguage.AmbiguousLR0ParseTableException;
import larp.parser.contextfreelanguage.LR0AcceptAction;
import larp.parser.contextfreelanguage.LR0GotoAction;
import larp.parser.contextfreelanguage.LR0Parser;
import larp.parser.contextfreelanguage.LR0ParseTable;
import larp.parser.contextfreelanguage.LR0ProductionSetDFAState;
import larp.parser.contextfreelanguage.LR0ReduceAction;
import larp.parser.contextfreelanguage.LR0ShiftAction;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.parsetree.contextfreelanguage.EndOfStringNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.TerminalNode;

import java.util.HashSet;

public class LR0ParserTest
{
    @Test
    public void testAcceptsReturnsTrueForCorrectCharacterInSingleCharacterCFG() throws AmbiguousLR0ParseTableException
    {
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

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
        LR0ProductionSetDFAState state1 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());
        LR0ProductionSetDFAState state2 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());
        LR0ProductionSetDFAState state3 = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

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
    public void testAcceptsReturnsTrueForMultiCharacterCFG()
    {
        throw new RuntimeException();
    }

    @Test
    public void testStructureEqualsReturnsTrueWhenParseTablesHaveSameStructure() throws AmbiguousLR0ParseTableException
    {
        ContextFreeGrammar cfg = new ContextFreeGrammar();
        cfg.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));
        cfg.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        LR0ProductionSetDFAState startState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());
        LR0ParseTable parseTable = new LR0ParseTable(cfg, startState);
        parseTable.addCell(startState, new TerminalNode("a"), new LR0ReduceAction(0));
        parseTable.addCell(startState, new TerminalNode("b"), new LR0ReduceAction(1));
        LR0Parser parser = new LR0Parser(parseTable);

        LR0ProductionSetDFAState otherStartState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());
        LR0ParseTable otherParseTable = new LR0ParseTable(cfg, otherStartState);
        otherParseTable.addCell(otherStartState, new TerminalNode("a"), new LR0ReduceAction(0));
        otherParseTable.addCell(otherStartState, new TerminalNode("b"), new LR0ReduceAction(1));
        LR0Parser otherParser = new LR0Parser(otherParseTable);

        assertTrue(parser.structureEquals(otherParser));
    }

    @Test
    public void testStructureEqualsReturnsFalseWhenParseTablesHaveDifferentStructure() throws AmbiguousLR0ParseTableException
    {
        ContextFreeGrammar cfg = new ContextFreeGrammar();
        cfg.addProduction(new NonTerminalNode("S"), new TerminalNode("a"));
        cfg.addProduction(new NonTerminalNode("S"), new TerminalNode("b"));

        LR0ProductionSetDFAState startState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());
        LR0ParseTable parseTable = new LR0ParseTable(cfg, startState);
        parseTable.addCell(startState, new TerminalNode("a"), new LR0ReduceAction(0));
        parseTable.addCell(startState, new TerminalNode("b"), new LR0ReduceAction(1));
        LR0Parser parser = new LR0Parser(parseTable);

        LR0ProductionSetDFAState otherStartState = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());
        LR0ParseTable otherParseTable = new LR0ParseTable(cfg, otherStartState);
        otherParseTable.addCell(otherStartState, new TerminalNode("a"), new LR0ReduceAction(0));
        otherParseTable.addCell(otherStartState, new TerminalNode("b"), new LR0ReduceAction(0));
        LR0Parser otherParser = new LR0Parser(otherParseTable);

        assertFalse(parser.structureEquals(otherParser));
    }
}
