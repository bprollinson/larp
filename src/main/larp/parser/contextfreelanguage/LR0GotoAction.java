package larp.parser.contextfreelanguage;

import larp.parser.regularlanguage.State;

public class LR0GotoAction implements LR0ParseTableAction
{
    private State nextState;

    public LR0GotoAction(State nextState)
    {
        this.nextState = nextState;
    }

    public boolean supportsTransition()
    {
        return true;
    }

    public State getNextState()
    {
        return this.nextState;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof LR0GotoAction))
        {
            return false;
        }

        return this.nextState.equals(((LR0GotoAction)other).getNextState());
    }
}
