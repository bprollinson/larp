package larp.automaton;

import larp.ComparableStructure;

import java.util.ArrayList;
import java.util.List;

public abstract class State implements ComparableStructure
{
    private String name;
    private boolean accepting;
    protected List<StateTransition> transitions;

    public State(String name, boolean accepting)
    {
        this.name = name;
        this.accepting = accepting;
        this.transitions = new ArrayList<StateTransition>();
    }

    public void addTransition(StateTransition transition)
    {
        this.transitions.add(transition);
    }

    public boolean isAccepting()
    {
        return this.accepting;
    }

    public void setAccepting(boolean accepting)
    {
        this.accepting = accepting;
    }

    public int countTransitions()
    {
        return this.transitions.size();
    }

    public List<StateTransition> getTransitions()
    {
        return this.transitions;
    }

    public boolean structureEquals(Object other)
    {
        if (!(other instanceof State))
        {
            return false;
        }

        State otherState = (State)other;
        List<State> ourCoveredStates = new ArrayList<State>();
        List<State> otherCoveredStates = new ArrayList<State>();

        return this.equalsState(otherState, ourCoveredStates, otherCoveredStates);
    }

    private boolean equalsState(State otherState, List<State> ourCoveredStates, List<State> otherCoveredStates)
    {
        if (this.accepting != otherState.isAccepting())
        {
            return false;
        }

        if (transitions.size() != otherState.countTransitions())
        {
            return false;
        }

        List<StateTransition> ourTransitions = new ArrayList<StateTransition>(this.transitions);
        List<StateTransition> otherTransitions = new ArrayList<StateTransition>(otherState.getTransitions());

        List<State> ourNextCoveredStates = new ArrayList<State>(ourCoveredStates);
        ourNextCoveredStates.add(this);
        List<State> otherNextCoveredStates = new ArrayList<State>(otherCoveredStates);
        otherNextCoveredStates.add(otherState);

        while (ourTransitions.size() > 0)
        {
            boolean found = false;

            StateTransition current = ourTransitions.get(0);

            for (int i = 0; i < otherTransitions.size(); i++)
            {
                StateTransition otherTransition = otherTransitions.get(i);

                if (current.getInput() == otherTransition.getInput())
                {
                    int ourNextStatePosition = this.coveredStatesPosition(ourCoveredStates, current.getNextState());
                    boolean ourNextStateLoops = ourNextStatePosition != -1;
                    int otherNextStatePosition = this.coveredStatesPosition(otherCoveredStates, otherTransition.getNextState());
                    boolean otherNextStateLoops = otherNextStatePosition != -1;
                    boolean nextStatesLoop = ourNextStateLoops && otherNextStateLoops;

                    if (ourNextStateLoops && !nextStatesLoop || otherNextStateLoops && !nextStatesLoop)
                    {
                        return false;
                    }

                    if (nextStatesLoop && ourNextStatePosition != otherNextStatePosition)
                    {
                        return false;
                    }

                    boolean nextStatesEqual = false;
                    if (!nextStatesLoop)
                    {
                        nextStatesEqual = current.getNextState().equalsState(otherTransition.getNextState(), ourNextCoveredStates, otherNextCoveredStates);
                    }
                    boolean equal = nextStatesEqual || nextStatesLoop;
                    if (nextStatesEqual)
                    {
                        for (int j = 0; j < ourNextCoveredStates.size(); j++)
                        {
                            ourCoveredStates.add(ourNextCoveredStates.get(j));
                            otherCoveredStates.add(otherNextCoveredStates.get(j));
                        }
                    }

                    if (equal)
                    {
                        found = true;
                        ourTransitions.remove(0);
                        otherTransitions.remove(i);
                        break;
                    }
                }
            }

            if (!found)
            {
                return false;
            }
        }

        ourCoveredStates.add(this);
        otherCoveredStates.add(otherState);

        return true;
    }

    private int coveredStatesPosition(List<State> coveredStates, State state)
    {
        for (int i = 0; i < coveredStates.size(); i++)
        {
            if (coveredStates.get(i) == state)
            {
                return i;
            }
        }

        return -1;
    }
}
