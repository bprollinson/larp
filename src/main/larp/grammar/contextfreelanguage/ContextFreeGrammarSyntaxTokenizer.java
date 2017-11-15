package larp.grammar.contextfreelanguage;

import java.util.Vector;

public class ContextFreeGrammarSyntaxTokenizer
{
    private boolean inTerminal;
    private int numSeparators;
    private int numTerminals;
    private int numNonTerminals;

    public Vector<ContextFreeGrammarSyntaxToken> tokenize(String expression) throws ContextFreeGrammarSyntaxTokenizerException
    {
        this.inTerminal = false;
        this.numSeparators = 0;
        this.numTerminals = 0;
        this.numNonTerminals = 0;

        Vector<ContextFreeGrammarSyntaxToken> tokens = this.convertCharactersToTokens(expression);

        if (this.inTerminal)
        {
            throw new IncorrectContextFreeGrammarQuoteNestingException();
        }

        new ContextFreeGrammarStartingTokenAssertion(tokens).validate();

        if (this.numSeparators != 1)
        {
            throw new IncorrectContextFreeGrammarSeparatorException();
        }

        return this.correctEpsilonSetupInTokens(tokens);
    }

    private Vector<ContextFreeGrammarSyntaxToken> convertCharactersToTokens(String expression)
    {
        Vector<ContextFreeGrammarSyntaxToken> tokens = new Vector<ContextFreeGrammarSyntaxToken>();

        String buffer = "";
        int numEpsilons = 0;

        for (int i = 0; i < expression.length(); i++)
        {
            char currentCharacter = expression.charAt(i);
            if (currentCharacter == ':')
            {
                if (buffer.length() > 0)
                {
                    tokens.add(new NonTerminalToken(buffer));
                    this.numNonTerminals++;
                }
                buffer = "";
                this.numSeparators++;
                tokens.add(new SeparatorToken());
            }
            else if (currentCharacter == '"' && !this.inTerminal)
            {
                if (buffer.length() > 0)
                {
                    tokens.add(new NonTerminalToken(buffer));
                    this.numNonTerminals++;
                    buffer = "";
                }
                this.inTerminal = true;
            }
            else if (currentCharacter == '"' && this.inTerminal)
            {
                if (buffer.length() == 0)
                {
                    tokens.add(new EpsilonToken());
                    numEpsilons++;
                }
                else
                {
                    tokens.add(new TerminalToken(buffer));
                    this.numTerminals++;
                }
                buffer = "";
                this.inTerminal = false;
            }
            else if (currentCharacter == ' ' && !this.inTerminal)
            {
                if (buffer.length() > 0)
                {
                    tokens.add(new NonTerminalToken(buffer));
                    this.numNonTerminals++;
                }
                buffer = "";
            }
            else
            {
                buffer += currentCharacter;
            }
        }

        if (buffer.length() > 0)
        {
            tokens.add(new NonTerminalToken(buffer));
            this.numNonTerminals++;
        }

        return tokens;
    }

    private Vector<ContextFreeGrammarSyntaxToken> correctEpsilonSetupInTokens(Vector<ContextFreeGrammarSyntaxToken> tokens)
    {
        Vector<ContextFreeGrammarSyntaxToken> correctedTokens = new Vector<ContextFreeGrammarSyntaxToken>();

        boolean epsilonAdded = false;

        for (int i = 0; i < tokens.size(); i++)
        {
            if (!(tokens.get(i) instanceof EpsilonToken))
            {
                correctedTokens.add(tokens.get(i));
            }

            if (tokens.get(i) instanceof EpsilonToken && this.numTerminals == 0 && this.numNonTerminals == 1 && !epsilonAdded)
            {
                correctedTokens.add(tokens.get(i));
                epsilonAdded = true;
            }
        }

        return correctedTokens;
    }
}
