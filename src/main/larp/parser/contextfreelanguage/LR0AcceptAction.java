package larp.parser.contextfreelanguage;

public class LR0AcceptAction implements LR0ParseTableAction
{
    public boolean isRowLevelAction()
    {
        return false;
    }

    public boolean equals(Object other)
    {
        return other instanceof LR0AcceptAction;
    }
}
