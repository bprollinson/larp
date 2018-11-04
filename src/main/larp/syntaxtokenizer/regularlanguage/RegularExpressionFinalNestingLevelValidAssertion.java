/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.syntaxtokenizer.regularlanguage;

import larp.assertion.Assertion;

public class RegularExpressionFinalNestingLevelValidAssertion implements Assertion
{
    private int nestingLevel;

    public RegularExpressionFinalNestingLevelValidAssertion(int nestingLevel)
    {
        this.nestingLevel = nestingLevel;
    }

    public void validate() throws IncorrectRegularExpressionNestingException
    {
        if (this.nestingLevel != 0)
        {
            throw new IncorrectRegularExpressionNestingException();
        }
    }
}
