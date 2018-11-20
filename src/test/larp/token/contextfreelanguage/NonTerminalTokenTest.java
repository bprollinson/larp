/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.token.contextfreelanguage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class NonTerminalTokenTest
{
    @Test
    public void testEqualsReturnsTrueForTokenWithSameClassAndName()
    {
        NonTerminalToken token = new NonTerminalToken("S");

        assertEquals(new NonTerminalToken("S"), token);
    }

    @Test
    public void testEqualsReturnsFalseForTokenWithSameClassAndDifferentName()
    {
        NonTerminalToken token = new NonTerminalToken("S");

        assertNotEquals(new NonTerminalToken("T"), token);
    }

    @Test
    public void testEqualsReturnsFalseForTokenWithDifferentClass()
    {
        NonTerminalToken token = new NonTerminalToken("S");

        assertNotEquals(new SeparatorToken(), token);
    }
}
