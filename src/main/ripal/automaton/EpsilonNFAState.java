/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package ripal.automaton;

public class EpsilonNFAState extends State<Character, EpsilonNFAState>
{
    public EpsilonNFAState(String name, boolean accepting)
    {
        super(name, accepting);
    }
}
