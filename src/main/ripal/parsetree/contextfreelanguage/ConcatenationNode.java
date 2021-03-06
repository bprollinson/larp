/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package ripal.parsetree.contextfreelanguage;

public class ConcatenationNode extends Node
{
    public void addChild(Node childNode)
    {
        this.childNodes.add(childNode);
    }
}
