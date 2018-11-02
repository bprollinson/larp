import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

import larp.parser.contextfreelanguage.LR0ParseStack;
import larp.parser.contextfreelanguage.LR0ProductionSetDFAState;
import larp.parsetree.contextfreelanguage.ContextFreeGrammarSyntaxNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;

import java.util.HashSet;

public class LR0ParseStackTest
{
    @Test
    public void testPeekReturnsTopObject()
    {
        LR0ProductionSetDFAState state = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

        LR0ParseStack stack = new LR0ParseStack();
        stack.push(state);
        stack.push(new NonTerminalNode("A"));

        assertEquals(new NonTerminalNode("A"), stack.peek());
    }

    @Test
    public void testPeekThrowsExceptionWhenStackIsEmpty()
    {
        throw new RuntimeException();
    }

    @Test
    public void testPopReturnsAndRemovesTopObject()
    {
        LR0ProductionSetDFAState state = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

        LR0ParseStack stack = new LR0ParseStack();
        stack.push(state);
        stack.push(new NonTerminalNode("A"));

        LR0ParseStack expectedStack = new LR0ParseStack();
        expectedStack.push(state);

        assertEquals(new NonTerminalNode("A"), stack.pop());
        assertEquals(expectedStack, stack);
    }

    @Test
    public void testPopThrowsExceptionWhenStackIsEmpty()
    {
        throw new RuntimeException();
    }

    @Test
    public void testGetTopStateReturnsTopElement()
    {
        LR0ProductionSetDFAState state = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

        LR0ParseStack stack = new LR0ParseStack();
        stack.push(new LR0ProductionSetDFAState("", true, new HashSet<ContextFreeGrammarSyntaxNode>()));
        stack.push(new NonTerminalNode("A"));
        stack.push(state);

        assertEquals(state, stack.getTopState());
    }

    @Test
    public void testGetTopStateReturnsLowerElement()
    {
        LR0ProductionSetDFAState state = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

        LR0ParseStack stack = new LR0ParseStack();
        stack.push(new LR0ProductionSetDFAState("", true, new HashSet<ContextFreeGrammarSyntaxNode>()));
        stack.push(new NonTerminalNode("A"));
        stack.push(state);
        stack.push(new NonTerminalNode("B"));

        assertEquals(state, stack.getTopState());
    }

    @Test
    public void testGetTopStateThrowsExceptionWhenStateNotFound()
    {
        throw new RuntimeException();
    }

    @Test
    public void testEqualsReturnsTrueForStacksContainingSameObjects()
    {
        LR0ProductionSetDFAState state = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

        LR0ParseStack stack = new LR0ParseStack();
        stack.push(state);
        stack.push(new NonTerminalNode("A"));

        LR0ParseStack otherStack = new LR0ParseStack();
        otherStack.push(state);
        otherStack.push(new NonTerminalNode("A"));

        assertEquals(otherStack, stack);
    }

    @Test
    public void testEqualsReturnsFalseForStacksContainingDifferentObjects()
    {
        LR0ProductionSetDFAState state = new LR0ProductionSetDFAState("", false, new HashSet<ContextFreeGrammarSyntaxNode>());

        LR0ParseStack stack = new LR0ParseStack();
        stack.push(state);
        stack.push(new NonTerminalNode("A"));

        LR0ParseStack otherStack = new LR0ParseStack();
        otherStack.push(state);
        otherStack.push(new NonTerminalNode("B"));

        assertNotEquals(otherStack, stack);
    }
}