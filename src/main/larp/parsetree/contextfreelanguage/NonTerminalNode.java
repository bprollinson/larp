package larp.parsetree.contextfreelanguage;

public class NonTerminalNode extends ContextFreeGrammarSyntaxNode
{
    private String name;

    public NonTerminalNode(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean equals(Object other)
    {
        if (!super.equals(other))
        {
            return false;
        }

        return this.name.equals(((NonTerminalNode)other).getName());
    }

    public int hashCode()
    {
        return super.hashCode() + this.name.hashCode();
    }
}
