import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.grammar.contextfreelanguage.ContextFreeGrammar;
import larp.parser.contextfreelanguage.LR0ParseTable;
import larp.syntaxcompiler.contextfreelanguage.ContextFreeGrammarLR0SyntaxCompiler;

public class ContextFreeGrammarLR0SyntaxCompilerTest
{
    @Test
    public void testCompileReturnsEmptyParseTableForEmptyCFG()
    {
        ContextFreeGrammarLR0SyntaxCompiler compiler = new ContextFreeGrammarLR0SyntaxCompiler();

        ContextFreeGrammar grammar = new ContextFreeGrammar();
        LR0ParseTable expectedTable = new LR0ParseTable(grammar);

        assertEquals(expectedTable, compiler.compile(grammar));
    }

    @Test
    public void testCompileReturnsParseTableForSingleCharacterProductionCFG()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileReturnsParseTableForSingleNonTerminalProductionCFG()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileReturnsParseTableForSingleNonTerminalAndSingleTerminalProductionCFG()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileHandlesMultipleNonTrivialProductionsWithinTheSameState()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileReturnsParseTableWithNonTerminalChain()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileReturnsParseTableWithTerminalChain()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileReturnsTableForCFGWithDFAContainingCycle()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileThrowsExceptionForShiftReduceConflict()
    {
        assertEquals(0, 1);
    }

    @Test
    public void testCompileThrowsExceptionForReduceReduceConflict()
    {
        assertEquals(0, 1);
    }
}
