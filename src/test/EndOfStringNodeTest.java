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
import larp.parsetree.contextfreelanguage.EndOfStringNode;

public class EndOfStringNodeTest
{
    @Test
    public void testEqualsReturnsTrueForNodeWithSameClass()
    {
        EndOfStringNode node = new EndOfStringNode();

        assertEquals(new EndOfStringNode(), node);
    }

    @Test
    public void testEqualsReturnsFalseForNodeWithDifferentClass()
    {
        EndOfStringNode node = new EndOfStringNode();

        assertNotEquals(new ConcatenationNode(), node);
    }
}
