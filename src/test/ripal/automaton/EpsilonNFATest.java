/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package ripal.automaton;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class EpsilonNFATest
{
    @Test
    public void testGetStartStateReturnsEpsilonNFAState()
    {
        EpsilonNFAState expectedStartState = new EpsilonNFAState("S0", true);
        EpsilonNFA nfa = new EpsilonNFA(expectedStartState);

        EpsilonNFAState startState = nfa.getStartState();
        assertEquals(expectedStartState, startState);
    }
}
