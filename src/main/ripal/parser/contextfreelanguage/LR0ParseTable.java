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
import ripal.parsetree.contextfreelanguage.Node;
import ripal.util.PairToValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LR0ParseTable implements ComparableStructure
{
    private Grammar grammar;
    private State startState;
    private int size;
    private PairToValueMap<State, Node, LR0ParseTableAction> cells;

    public LR0ParseTable(Grammar grammar, State startState)
    {
        this.grammar = grammar;
        this.startState = startState;
        this.size = 0;
        this.cells = new PairToValueMap<State, Node, LR0ParseTableAction>();
    }

    public State getStartState()
    {
        return this.startState;
    }

    public void addCell(State state, Node syntaxNode, LR0ParseTableAction action) throws AmbiguousLR0ParseTableException
    {
        new LR0ParseTableCellAvailableAssertion(this, state, syntaxNode, action).validate();

        this.size++;
        this.cells.put(state, syntaxNode, action);
    }

    public LR0ParseTableAction getCell(State state, Node syntaxNode)
    {
        return this.cells.get(state, syntaxNode);
    }

    public Grammar getGrammar()
    {
        return this.grammar;
    }

    public int size()
    {
        return this.size;
    }

    public PairToValueMap<State, Node, LR0ParseTableAction> getCells()
    {
        return this.cells;
    }

    public boolean cellsEqual(PairToValueMap<State, Node, LR0ParseTableAction> otherCells)
    {
        return this.cells.equals(otherCells);
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof LR0ParseTable))
        {
            return false;
        }

        if (!this.grammar.equals(((LR0ParseTable)other).getGrammar()))
        {
            return false;
        }

        if (this.startState != ((LR0ParseTable)other).getStartState())
        {
            return false;
        }

        return ((LR0ParseTable)other).cellsEqual(this.cells);
    }

    public boolean structureEquals(Object other)
    {
        if (!(other instanceof LR0ParseTable))
        {
            return false;
        }

        LR0ParseTable otherTable = (LR0ParseTable)other;
        if (!this.grammar.equals(otherTable.getGrammar()))
        {
            return false;
        }

        List<State> ourCoveredStates = new ArrayList<State>();
        List<State> otherCoveredStates = new ArrayList<State>();

        return this.buildStateComparator().equalsState(this, this.getStartState(), otherTable, otherTable.getStartState(), ourCoveredStates, otherCoveredStates);
    }

    protected LR0ParseTableStateComparator buildStateComparator()
    {
        return new LR0ParseTableStateComparator();
    }

    protected class LR0ParseTableStateComparator
    {
        public boolean equalsState(LR0ParseTable table, State startState, LR0ParseTable otherTable, State otherStartState, List<State> ourCoveredStates, List<State> otherCoveredStates)
        {
            int ourStatePosition = ourCoveredStates.indexOf(startState);
            int otherStatePosition = otherCoveredStates.indexOf(otherStartState);

            if (ourStatePosition != -1)
            {
                return ourStatePosition == otherStatePosition;
            }
            else if (otherStatePosition != -1)
            {
                return false;
            }

            ourCoveredStates.add(startState);
            otherCoveredStates.add(otherStartState);

            PairToValueMap<State, Node, LR0ParseTableAction> ourCells = table.getCells();
            Map<State, Map<Node, LR0ParseTableAction>> ourMap = ourCells.getMap();
            Map<Node, LR0ParseTableAction> ourRow = ourMap.get(startState);

            PairToValueMap<State, Node, LR0ParseTableAction> otherCells = otherTable.getCells();
            Map<State, Map<Node, LR0ParseTableAction>> otherMap = otherCells.getMap();
            Map<Node, LR0ParseTableAction> otherRow = otherMap.get(otherStartState);

            if (ourRow == null)
            {
                return otherRow == null;
            }
            if (!this.rowsHaveSameSize(ourRow, otherRow))
            {
                return false;
            }

            for (Map.Entry<Node, LR0ParseTableAction> ourCell: ourRow.entrySet())
            {
                Node node = ourCell.getKey();
                LR0ParseTableAction ourAction = ourCell.getValue();
                LR0ParseTableAction otherAction = null;
                if (otherRow != null)
                {
                    otherAction = otherRow.get(node);
                }

                if (!this.actionClassesEqual(ourAction, otherAction))
                {
                    return false;
                }
                if (!this.actionInstancesEqual(table, ourAction, otherTable, otherAction, ourCoveredStates, otherCoveredStates))
                {
                    return false;
                }
            }

            return true;
        }

        private boolean rowsHaveSameSize(Map<Node, LR0ParseTableAction> ourRow, Map<Node, LR0ParseTableAction> otherRow)
        {
            if (otherRow == null)
            {
                return false;
            }

            return ourRow.size() == otherRow.size();
        }

        private boolean actionClassesEqual(Object object1, Object object2)
        {
            if (object2 == null)
            {
                return false;
            }

            return object1.getClass().equals(object2.getClass());
        }

        private boolean actionInstancesEqual(LR0ParseTable table, LR0ParseTableAction ourAction, LR0ParseTable otherTable, LR0ParseTableAction otherAction, List<State> ourCoveredStates, List<State> otherCoveredStates)
        {
            if (ourAction.supportsTransition())
            {
                return this.equalsState(table, ourAction.getNextState(), otherTable, otherAction.getNextState(), ourCoveredStates, otherCoveredStates);
            }

            return ourAction.equals(otherAction);
        }
    }
}
