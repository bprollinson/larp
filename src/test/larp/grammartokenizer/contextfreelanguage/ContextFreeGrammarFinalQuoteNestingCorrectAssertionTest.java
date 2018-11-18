/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.grammartokenizer.contextfreelanguage;

import org.junit.Test;

public class ContextFreeGrammarFinalQuoteNestingCorrectAssertionTest
{
    @Test(expected = IncorrectContextFreeGrammarQuoteNestingException.class)
    public void testValidateThrowsExceptionWhenInTerminal() throws IncorrectContextFreeGrammarQuoteNestingException
    {
        ContextFreeGrammarFinalQuoteNestingCorrectAssertion assertion = new ContextFreeGrammarFinalQuoteNestingCorrectAssertion(true);

        assertion.validate();
    }

    @Test
    public void testValidateDoesNotThrowExceptionWhenNotInTerminal() throws IncorrectContextFreeGrammarQuoteNestingException
    {
        ContextFreeGrammarFinalQuoteNestingCorrectAssertion assertion = new ContextFreeGrammarFinalQuoteNestingCorrectAssertion(false);

        assertion.validate();
    }
}
