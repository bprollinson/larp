import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.token.regularlanguage.CharacterToken;
import larp.token.regularlanguage.CloseBraceToken;
import larp.token.regularlanguage.KleeneClosureToken;
import larp.token.regularlanguage.OpenBraceToken;
import larp.token.regularlanguage.OrToken;
import larp.token.regularlanguage.RegularExpressionSyntaxToken;
import larp.tokenizer.regularlanguage.IncorrectRegularExpressionNestingException;
import larp.tokenizer.regularlanguage.RegularExpressionSyntaxTokenizer;
import larp.tokenizer.regularlanguage.RegularExpressionSyntaxTokenizerException;

import java.util.ArrayList;
import java.util.List;

public class RegularExpressionSyntaxTokenizerTest
{
    @Test
    public void testTokenizerTokenizesEmptyString() throws RegularExpressionSyntaxTokenizerException
    {
        RegularExpressionSyntaxTokenizer tokenizer = new RegularExpressionSyntaxTokenizer();

        List<RegularExpressionSyntaxToken> result = tokenizer.tokenize("");
        List<RegularExpressionSyntaxToken> expectedResult = new ArrayList<RegularExpressionSyntaxToken>();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testTokenizerTokenizesString() throws RegularExpressionSyntaxTokenizerException
    {
        RegularExpressionSyntaxTokenizer tokenizer = new RegularExpressionSyntaxTokenizer();

        List<RegularExpressionSyntaxToken> result = tokenizer.tokenize("(a|b)*");
        List<RegularExpressionSyntaxToken> expectedResult = new ArrayList<RegularExpressionSyntaxToken>();
        expectedResult.add(new OpenBraceToken());
        expectedResult.add(new CharacterToken('a'));
        expectedResult.add(new OrToken());
        expectedResult.add(new CharacterToken('b'));
        expectedResult.add(new CloseBraceToken());
        expectedResult.add(new KleeneClosureToken());

        assertEquals(expectedResult, result);
    }

    @Test(expected = IncorrectRegularExpressionNestingException.class)
    public void testTokenizerThrowsExceptionForNegativeBracketNesting() throws RegularExpressionSyntaxTokenizerException
    {
        RegularExpressionSyntaxTokenizer tokenizer = new RegularExpressionSyntaxTokenizer();

        tokenizer.tokenize(")(");
    }

    @Test(expected = IncorrectRegularExpressionNestingException.class)
    public void testTokenizerThrowsExceptionForUnclosedBracketAtEndOfString() throws RegularExpressionSyntaxTokenizerException
    {
        RegularExpressionSyntaxTokenizer tokenizer = new RegularExpressionSyntaxTokenizer();

        tokenizer.tokenize("(");
    }

    @Test(expected = RegularExpressionSyntaxTokenizerException.class)
    public void testTokenizerThrowsExceptionForIncorrecTokenSequence() throws RegularExpressionSyntaxTokenizerException
    {
        RegularExpressionSyntaxTokenizer tokenizer = new RegularExpressionSyntaxTokenizer();

        tokenizer.tokenize("*");
    }
}
