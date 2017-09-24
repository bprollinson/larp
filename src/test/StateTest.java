import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import larp.automaton.State;
import larp.automaton.StateTransition;

public class StateTest
{
    @Test
    public void testEqualsReturnsTrueForSameAcceptsValueOnSingleState()
    {
        State state = new TestState("S0", true);

        assertTrue(state.equals(new TestState("S1", true)));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentAcceptsValueOnSingleState()
    {
        State state = new TestState("S0", true);

        assertFalse(state.equals(new TestState("S1", false)));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentNumberOfTransitions()
    {
        State state = new TestState("S0", true);
        state.addTransition(new StateTransition('a', state));

        assertFalse(state.equals(new TestState("S1", true)));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTransitionChracters()
    {
        State state = new TestState("S0", true);
        state.addTransition(new StateTransition('a', state));
        State otherState = new TestState("S1", true);
        otherState.addTransition(new StateTransition('b', otherState));

        assertFalse(state.equals(otherState));
    }

    @Test
    public void testEqualsReturnsTrueForSameTransitionChracters()
    {
        State state = new TestState("S0", true);
        state.addTransition(new StateTransition('a', new TestState("S1", true)));
        State otherState = new TestState("S2", true);
        otherState.addTransition(new StateTransition('a', new TestState("S3", true)));

        assertTrue(state.equals(otherState));
    }

    @Test
    public void testEqualsReturnsTrueForSameTransitionCharactersInDifferentOrder()
    {
        State state = new TestState("S0", true);
        state.addTransition(new StateTransition('a', new TestState("S1", true)));
        state.addTransition(new StateTransition('b', new TestState("S2", true)));
        State otherState = new TestState("S3", true);
        otherState.addTransition(new StateTransition('b', new TestState("S4", true)));
        otherState.addTransition(new StateTransition('a', new TestState("S5", true)));

        assertTrue(state.equals(otherState));
    }

    @Test
    public void testEqualsReturnsFalseForSubsequentStateInequality()
    {
        State state = new TestState("S0", true);
        state.addTransition(new StateTransition('a', new TestState("S1", true)));
        State otherState = new TestState("S2", true);
        otherState.addTransition(new StateTransition('a', new TestState("S3", false)));

        assertFalse(state.equals(otherState));
    }

    @Test
    public void testEqualsReturnsTrueForStateGraphContainingCycle()
    {
        State state = new TestState("S0", true);
        state.addTransition(new StateTransition('a', state));
        State otherState = new TestState("S0", true);
        otherState.addTransition(new StateTransition('a', otherState));

        assertTrue(state.equals(otherState));
    }

    @Test
    public void testEqualsReturnsFalseForStateGraphContainingDifferenceCycle()
    {
        State state1 = new TestState("S0", true);
        State state2 = new TestState("S1", true);
        State state3 = new TestState("S1", true);
        state1.addTransition(new StateTransition('a', state2));
        state2.addTransition(new StateTransition('a', state3));
        state3.addTransition(new StateTransition('a', state1));

        State otherState1 = new TestState("S0", true);
        State otherState2 = new TestState("S1", true);
        State otherState3 = new TestState("S1", true);
        otherState1.addTransition(new StateTransition('a', otherState2));
        otherState2.addTransition(new StateTransition('a', otherState3));
        otherState3.addTransition(new StateTransition('a', otherState2));

        assertFalse(state1.equals(otherState1));
    }

    @Test
    public void testEqualsReturnsFalseForUnmatchedLoopFoundInParallelRecursion()
    {
        State state1 = new TestState("S0", false);
        State state2 = new TestState("S1", false);
        State state3 = new TestState("S2", false);
        State state4 = new TestState("S3", false);
        state1.addTransition(new StateTransition('a', state2));
        state1.addTransition(new StateTransition('b', state3));
        state2.addTransition(new StateTransition('a', state4));
        state3.addTransition(new StateTransition('a', state4));

        State otherState1 = new TestState("S0", false);
        State otherState2 = new TestState("S1", false);
        State otherState3 = new TestState("S2", false);
        State otherState4 = new TestState("S3", false);
        State otherState5 = new TestState("S3", false);
        otherState1.addTransition(new StateTransition('a', otherState2));
        otherState1.addTransition(new StateTransition('b', otherState3));
        otherState2.addTransition(new StateTransition('a', otherState4));
        otherState3.addTransition(new StateTransition('a', otherState5));

        assertFalse(state1.equals(otherState1));
    }

    private class TestState extends State
    {
        public TestState(String name, boolean accepting)
        {
            super(name, accepting);
        }
    }
}
