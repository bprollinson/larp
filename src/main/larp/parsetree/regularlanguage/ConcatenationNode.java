package larp.parsetree.regularlanguage;

public class ConcatenationNode extends RegularExpressionSyntaxNode
{
    public void addChild(RegularExpressionSyntaxNode childNode)
    {
        this.childNodes.add(childNode);
    }
}
