/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package ripal.parser.contextfreelanguage;

import ripal.ComparableStructure;
import ripal.automaton.State;
import ripal.grammar.contextfreelanguage.Grammar;
import ripal.parsetree.contextfreelanguage.EndOfStringNode;
import ripal.parsetree.contextfreelanguage.EpsilonNode;
import ripal.parsetree.contextfreelanguage.Node;
import ripal.parsetree.contextfreelanguage.TerminalNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LR0Parser implements Parser, ComparableStructure
{
    private LR0ParseTable parseTable;
    private Grammar grammar;
    private List<Integer> appliedRules;

    public LR0Parser(LR0ParseTable parseTable)
    {
        this.parseTable = parseTable;
        this.grammar = parseTable.getGrammar();
        this.appliedRules = new ArrayList<Integer>();
    }

    public LR0ParseTable getParseTable()
    {
        return this.parseTable;
    }

    public boolean accepts(String inputString)
    {
        try
        {
            this.appliedRules = new ArrayList<Integer>();

            State currentState = this.parseTable.getStartState();

            LR0ParseStack stack = new LR0ParseStack();
            stack.push(currentState);

            while (true)
            {
                Node characterNode = this.calculateCharacterNode(inputString, stack);
                LR0ParseTableAction action = this.parseTable.getCell(currentState, characterNode);

                if (action == null)
                {
                    return false;
                }
                if (action.isAcceptAction())
                {
                    return true;
                }
                if (action.isShiftAction())
                {
                    inputString = inputString.substring(1);
                    stack.push(characterNode);
                }
                if (action.isReduceAction())
                {
                    this.applyReduction((LR0ReduceAction)action, stack);
                }

                State nextState = action.getNextState();
                if (nextState != null)
                {
                    currentState = nextState;
                    stack.push(nextState);
                }
                else
                {
                    currentState = stack.getTopState();
                }
            }
        }
        catch (LR0ParseStackEmptyException psee)
        {
            return false;
        }
    }

    private Node calculateCharacterNode(String inputString, LR0ParseStack stack) throws LR0ParseStackEmptyException
    {
        if (stack.peek() instanceof Node)
        {
            return (Node)stack.peek();
        }
        if (inputString.length() > 0)
        {
            String nextCharacter = inputString.substring(0, 1);
            return new TerminalNode(nextCharacter);
        }

        return new EndOfStringNode();
    }

    private void applyReduction(LR0ReduceAction action, LR0ParseStack stack) throws LR0ParseStackEmptyException
    {
        int productionIndex = ((LR0ReduceAction)action).getProductionIndex();

        this.appliedRules.add(productionIndex - 1);

        Node rootNode = this.grammar.getProduction(productionIndex);
        Node leftHandNode = rootNode.getChildNodes().get(0);
        List<Node> rightHandNodes = rootNode.getChildNodes().get(1).getChildNodes();
        int reduceSize = this.calculateReduceSize(rightHandNodes);

        for (int i = 0; i < reduceSize; i++)
        {
            stack.pop();
        }

        stack.push(leftHandNode);
    }

    private int calculateReduceSize(List<Node> rightHandNodes)
    {
        if (rightHandNodes.get(0) instanceof EpsilonNode)
        {
            return 0;
        }

        return 2 * rightHandNodes.size();
    }

    public List<Integer> getAppliedRules()
    {
        List<Integer> appliedRulesReversed = new ArrayList<Integer>(this.appliedRules);
        Collections.reverse(appliedRulesReversed);

        return appliedRulesReversed;
    }

    public boolean structureEquals(Object other)
    {
        if (!(other instanceof LR0Parser))
        {
            return false;
        }

        return this.parseTable.structureEquals(((LR0Parser)other).getParseTable());
    }
}
