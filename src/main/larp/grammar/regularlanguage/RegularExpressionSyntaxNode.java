package larp.grammar.regularlanguage;

import java.util.ArrayList;
import java.util.List;

public class RegularExpressionSyntaxNode
{
    protected List<RegularExpressionSyntaxNode> childNodes;

    public RegularExpressionSyntaxNode()
    {
        this.childNodes = new ArrayList<RegularExpressionSyntaxNode>();
    }

    public List<RegularExpressionSyntaxNode> getChildNodes()
    {
        return this.childNodes;
    }

    public boolean equals(Object other)
    {
        if (!this.getClass().equals(other.getClass()))
        {
            return false;
        }

        return this.childNodes.equals(((RegularExpressionSyntaxNode)other).getChildNodes());
    }
}
