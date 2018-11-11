/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.automaton.NFA;
import larp.automaton.NFAState;

public class NFATest
{
    @Test
    public void testGetStartStateReturnsNFAState()
    {
        NFAState expectedStartState = new NFAState("S0", true);
        NFA nfa = new NFA(expectedStartState);

        NFAState startState = nfa.getStartState();
        assertEquals(expectedStartState, startState);
    }
}
