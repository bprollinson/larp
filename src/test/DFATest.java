import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.parser.regularlanguage.DFA;
import larp.parser.regularlanguage.DFAState;
import larp.parser.regularlanguage.NFAState;
import larp.parser.regularlanguage.StateTransition;

public class DFATest
{
    @Test
    public void testGetStartStateReturnsDFAState()
    {
        assertTrue(false);
    }

    @Test
    public void testAcceptsReturnsFalseInNonExceptingState()
    {
        DFA dfa = this.buildDFA();

        assertFalse(dfa.accepts(""));
    }

    @Test
    public void testAcceptsReturnsTrueInExceptingState()
    {
        DFA dfa = this.buildDFA();

        assertTrue(dfa.accepts("a"));
    }

    @Test
    public void testAcceptsReturnsFalseForMissingTransition()
    {
        DFA dfa = this.buildDFA();

        assertFalse(dfa.accepts("ab"));
    }

    @Test
    public void testAcceptsReturnsFalseForInitialNonDFAState()
    {
        NFAState state0 = new NFAState("S0", true);
        DFA dfa = new DFA(state0);

        assertFalse(dfa.accepts(""));
    }

    @Test
    public void testAcceptsReturnsFalseForSubsequentNonDFAState()
    {
        DFAState state0 = new DFAState("S0", false);
        NFAState state1 = new NFAState("S1", true);
        state0.addTransition(new StateTransition<Character>('a', state1));
        DFA dfa = new DFA(state0);

        assertFalse(dfa.accepts("a"));
    }

    private DFA buildDFA()
    {
        DFAState state0 = new DFAState("S0", false);
        DFAState state1 = new DFAState("S1", true);
        state0.addTransition(new StateTransition<Character>('a', state1));

        return new DFA(state0);
    }
}
