/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.parsercompiler.contextfreelanguage;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.grammar.contextfreelanguage.Grammar;
import larp.parsetree.contextfreelanguage.EndOfStringNode;
import larp.parsetree.contextfreelanguage.NonTerminalNode;
import larp.parsetree.contextfreelanguage.TerminalNode;

public class ContextFreeGrammarAugmentorTest
{
    @Test
    public void testAugmentAddsNewStartStateToValidGrammar()
    {
        ContextFreeGrammarAugmentor augmentor = new ContextFreeGrammarAugmentor();

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        grammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));

        Grammar expectedGrammar = new Grammar();
        expectedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        expectedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        expectedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"));
        assertEquals(expectedGrammar, augmentor.augment(grammar));
    }

    @Test
    public void testAugmentCalculatesNewStartSymbolNameFromExistingStartSymbol()
    {
        ContextFreeGrammarAugmentor augmentor = new ContextFreeGrammarAugmentor();

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("Q"), new NonTerminalNode("R"));

        Grammar expectedGrammar = new Grammar();
        expectedGrammar.addProduction(new NonTerminalNode("Q'"), new NonTerminalNode("Q"), new EndOfStringNode());
        expectedGrammar.addProduction(new NonTerminalNode("Q"), new NonTerminalNode("R"));
        assertEquals(expectedGrammar, augmentor.augment(grammar));
    }

    @Test
    public void testAugmentResolvesNamingConflictRelatedtoNewStartSymbolName()
    {
        ContextFreeGrammarAugmentor augmentor = new ContextFreeGrammarAugmentor();

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("S'"));
        grammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S''"));
        grammar.addProduction(new NonTerminalNode("S''"), new NonTerminalNode("S'''"));

        Grammar expectedGrammar = new Grammar();
        expectedGrammar.addProduction(new NonTerminalNode("S''''"), new NonTerminalNode("S"), new EndOfStringNode());
        expectedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("S'"));
        expectedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S''"));
        expectedGrammar.addProduction(new NonTerminalNode("S''"), new NonTerminalNode("S'''"));
        assertEquals(expectedGrammar, augmentor.augment(grammar));
    }

    @Test
    public void testAugmentSplitsMultiCharacterTerminalNodeIntoMultipleNodes()
    {
        ContextFreeGrammarAugmentor augmentor = new ContextFreeGrammarAugmentor();

        Grammar grammar = new Grammar();
        grammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        grammar.addProduction(new NonTerminalNode("A"), new TerminalNode("abc"));

        Grammar expectedGrammar = new Grammar();
        expectedGrammar.addProduction(new NonTerminalNode("S'"), new NonTerminalNode("S"), new EndOfStringNode());
        expectedGrammar.addProduction(new NonTerminalNode("S"), new NonTerminalNode("A"));
        expectedGrammar.addProduction(new NonTerminalNode("A"), new TerminalNode("a"), new TerminalNode("b"), new TerminalNode("c"));
        assertEquals(expectedGrammar, augmentor.augment(grammar));
    }
}
