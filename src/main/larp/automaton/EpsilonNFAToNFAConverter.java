package larp.automaton;

import java.util.List;
import java.util.Vector;

public class EpsilonNFAToNFAConverter
{
    public NFA convert(EpsilonNFA nfa)
    {
        return new NFA(this.convertNode(nfa.getStartState(), new Vector<State>(), new Vector<State>()));
    }

    private State convertNode(State epsilonNFAState, Vector<State> coveredEpsilonNFAStates, Vector<State> coveredNFAStates)
    {
        int firstIndexOfState = coveredEpsilonNFAStates.indexOf(epsilonNFAState);
        if (firstIndexOfState != -1)
        {
            return coveredNFAStates.get(firstIndexOfState);
        }

        State startState = new NFAState("", false);
        coveredEpsilonNFAStates.add(epsilonNFAState);
        coveredNFAStates.add(startState);

        boolean epsilonToAccepting = this.epsilonToAccepting(epsilonNFAState, new Vector<State>());
        if (epsilonToAccepting)
        {
            startState.setAccepting(true);
        }

        List<StateTransition> transitions = epsilonNFAState.getTransitions();
        for (int i = 0; i < transitions.size(); i++)
        {
            StateTransition transition = transitions.get(i);
            if (transition.getInput() != null)
            {
                startState.addTransition(new StateTransition(transition.getInput(), this.convertNode(transition.getNextState(), coveredEpsilonNFAStates, coveredNFAStates)));
            }

            if (transition.getInput() == null)
            {
                Vector<StateTransition> tangibleStateTransitions = this.tangibleStateTransitions(transition, new Vector<StateTransition>());

                for (int j = 0; j < tangibleStateTransitions.size(); j++)
                {
                    StateTransition nextTransition = tangibleStateTransitions.get(j);
                    startState.addTransition(new StateTransition(nextTransition.getInput(), this.convertNode(nextTransition.getNextState(), coveredEpsilonNFAStates, coveredNFAStates)));
                }
            }
        }

        return startState;
    }

    private boolean epsilonToAccepting(State startState, Vector<State> processedStates)
    {
        if (processedStates.contains(startState))
        {
            return false;
        }
        processedStates.add(startState);

        if (startState.isAccepting())
        {
            return true;
        }

        List<StateTransition> transitions = startState.getTransitions();
        for (int i = 0; i < transitions.size(); i++)
        {
            StateTransition transition = transitions.get(i);
            if (transition.getInput() == null && this.epsilonToAccepting(transition.getNextState(), processedStates))
            {
                return true;
            }
        }

        return false;
    }

    private Vector<StateTransition> tangibleStateTransitions(StateTransition startTransition, Vector<StateTransition> processedTransitions)
    {
        Vector<StateTransition> result = new Vector<StateTransition>();
        if (processedTransitions.contains(startTransition))
        {
            return result;
        }
        processedTransitions.add(startTransition);

        State nextState = startTransition.getNextState();
        List<StateTransition> nextStateTransitions = nextState.getTransitions();

        for (int i = 0; i < nextStateTransitions.size(); i++)
        {
            StateTransition nextTransition = nextStateTransitions.get(i);
            if (nextTransition.getInput() != null)
            {
                result.add(nextTransition);
            }
            else
            {
                Vector<StateTransition> subsequentTransitions = this.tangibleStateTransitions(nextTransition, processedTransitions);
                result.addAll(subsequentTransitions);
            }
        }

        return result;
    }
}
