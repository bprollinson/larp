import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.automaton.EpsilonNFA;
import larp.automaton.EpsilonNFAState;
import larp.automaton.EpsilonNFAToNFAConverter;
import larp.automaton.NFA;
import larp.automaton.NFAState;
import larp.automaton.StateTransition;

public class EpsilonNFAToNFAConverterTest
{
    @Test
    public void testNonEpsilonNFARemainsUnchanged()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        EpsilonNFAState expectedState1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState expectedState2 = new EpsilonNFAState("S1", true);
        expectedState1.addTransition(new StateTransition('a', expectedState2));
        EpsilonNFA expectedEpsilonNFA = new EpsilonNFA(expectedState1);

        assertEquals(expectedEpsilonNFA, converter.convert(expectedEpsilonNFA));
    }

    @Test
    public void testStartStateSingleEpsilonTransitionEliminated()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", false);
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        state1.addTransition(new StateTransition(null, state2));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }

    @Test
    public void testNonStartStateSingleEpsilonTransitionEliminated()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", false);
        NFAState expectedState2 = new NFAState("S0", false);
        expectedState1.addTransition(new StateTransition('a', expectedState2));
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        EpsilonNFAState state3 = new EpsilonNFAState("S2", false);
        state1.addTransition(new StateTransition('a', state2));
        state2.addTransition(new StateTransition(null, state3));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }

    @Test
    public void testStartStateFinalStatusObtainedFromStateAfterEpsilonTransition()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", true);
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        EpsilonNFAState state3 = new EpsilonNFAState("S1", true);
        state1.addTransition(new StateTransition(null, state2));
        state1.addTransition(new StateTransition(null, state3));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }

    @Test
    public void testNonStartStateFinalStatusObtainedFromStateAfterEpsilonTransition()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", false);
        NFAState expectedState2 = new NFAState("S1", true);
        expectedState1.addTransition(new StateTransition('a', expectedState2));
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        EpsilonNFAState state3 = new EpsilonNFAState("S2", false);
        EpsilonNFAState state4 = new EpsilonNFAState("S3", true);
        state1.addTransition(new StateTransition('a', state2));
        state2.addTransition(new StateTransition(null, state3));
        state2.addTransition(new StateTransition(null, state4));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }

    @Test
    public void testSingleTransitionTransferredFromAfterEpsilonTransition()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", false);
        NFAState expectedState2 = new NFAState("S1", true);
        expectedState1.addTransition(new StateTransition('a', expectedState2));
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        EpsilonNFAState state3 = new EpsilonNFAState("S2", true);
        state1.addTransition(new StateTransition(null, state2));
        state2.addTransition(new StateTransition('a', state3));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }

    @Test
    public void testFinalStatusObtainedFromSequenceOfEpsilonTransitions()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", true);
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        EpsilonNFAState state3 = new EpsilonNFAState("S2", true);
        state1.addTransition(new StateTransition(null, state2));
        state2.addTransition(new StateTransition(null, state3));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }

    @Test
    public void testMultipleEpsilonTransitionsNavigated()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", false);
        NFAState expectedState2 = new NFAState("S1", false);
        NFAState expectedState3 = new NFAState("S2", false);
        expectedState1.addTransition(new StateTransition('a', expectedState2));
        expectedState1.addTransition(new StateTransition('b', expectedState3));
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        EpsilonNFAState state3 = new EpsilonNFAState("S2", false);
        EpsilonNFAState state4 = new EpsilonNFAState("S3", false);
        EpsilonNFAState state5 = new EpsilonNFAState("S4", false);
        EpsilonNFAState state6 = new EpsilonNFAState("S5", false);
        state1.addTransition(new StateTransition(null, state2));
        state2.addTransition(new StateTransition(null, state3));
        state3.addTransition(new StateTransition('a', state4));
        state2.addTransition(new StateTransition(null, state5));
        state5.addTransition(new StateTransition('b', state6));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }

    @Test
    public void testCycleNavigatedForConcreteTransitions()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        EpsilonNFAState expectedState1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState expectedState2 = new EpsilonNFAState("S1", false);
        EpsilonNFAState expectedState3 = new EpsilonNFAState("S2", false);
        expectedState1.addTransition(new StateTransition('a', expectedState2));
        expectedState2.addTransition(new StateTransition('b', expectedState3));
        expectedState3.addTransition(new StateTransition('c', expectedState1));
        EpsilonNFA expectedEpsilonNFA = new EpsilonNFA(expectedState1);

        assertEquals(expectedEpsilonNFA, converter.convert(expectedEpsilonNFA));
    }

    @Test
    public void testCycleNavigatedForFinalStateCalculationFromEpsilonTransitions()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", false);
        NFAState expectedState2 = new NFAState("S1", false);
        expectedState1.addTransition(new StateTransition('a', expectedState2));
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        state1.addTransition(new StateTransition('a', state2));
        state2.addTransition(new StateTransition(null, state2));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }

    @Test
    public void testCycleNavigatedForNextTransitionCalculationsFromEpsilonTransitions()
    {
        EpsilonNFAToNFAConverter converter = new EpsilonNFAToNFAConverter();

        NFAState expectedState1 = new NFAState("S0", false);
        expectedState1.addTransition(new StateTransition('a', expectedState1));
        NFA expectedNFA = new NFA(expectedState1);

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        state1.addTransition(new StateTransition(null, state2));
        state2.addTransition(new StateTransition('a', state1));
        EpsilonNFA epsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedNFA, converter.convert(epsilonNFA));
    }
}
