/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.parsercompiler.contextfreelanguage;

import larp.grammar.contextfreelanguage.Grammar;
import larp.parser.contextfreelanguage.LR0ParseTable;

public class LR1ParserCompiler
{
    public LR0ParseTable compile(Grammar grammar)
    {
        return new LR0ParseTable(grammar, null);
    }
}
