/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

import larp.parsetree.contextfreelanguage.ConcatenationNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;

public class NonTerminalNodeTest
{
    @Test
    public void testEqualsReturnsTrueForNodeWithSameClassAndName()
    {
        NonTerminalNode node = new NonTerminalNode("S");

        assertEquals(new NonTerminalNode("S"), node);
    }

    @Test
    public void testEqualsReturnsFalseForNodeWithSameClassAndDifferentName()
    {
        NonTerminalNode node = new NonTerminalNode("S");

        assertNotEquals(new NonTerminalNode("T"), node);
    }

    @Test
    public void testEqualsReturnsFalseForNodeWithDifferentClass()
    {
        NonTerminalNode node = new NonTerminalNode("S");

        assertNotEquals(new ConcatenationNode(), node);
    }
}
