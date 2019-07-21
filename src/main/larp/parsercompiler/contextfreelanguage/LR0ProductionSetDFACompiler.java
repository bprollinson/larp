/*
 * Copyright (C) 2017 Brendan Rollinson-Lorimer
 *
 * This library is licensed under LGPL v2.1.
 * See LICENSE.md for details.
 */

package larp.parsercompiler.contextfreelanguage;

import larp.automaton.StateTransition;
import larp.grammar.contextfreelanguage.Grammar;
import larp.parser.contextfreelanguage.LR0ProductionSetDFA;
import larp.parser.contextfreelanguage.LR0ProductionSetDFAState;
import larp.parsetree.contextfreelanguage.EndOfStringNode;
import larp.parsetree.contextfreelanguage.EpsilonNode;
import larp.parsetree.contextfreelanguage.Node;
import larp.util.ValueToSetMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LR0ProductionSetDFACompiler
{
    private GrammarAugmentor grammarAugmentor;
    private GrammarClosureCalculator closureCalculator;
    private ProductionNodeDotRepository productionNodeDotRepository;
    private Map<Set<GrammarClosureRule>, LR0ProductionSetDFAState> productionSetToStateMap;

    public LR0ProductionSetDFACompiler()
    {
        this.grammarAugmentor = new GrammarAugmentor();
        this.closureCalculator = new GrammarClosureCalculator();
        this.productionNodeDotRepository = new ProductionNodeDotRepository();
    }

    public LR0ProductionSetDFA compile(Grammar grammar)
    {
        this.productionSetToStateMap = new HashMap<Set<GrammarClosureRule>, LR0ProductionSetDFAState>();
        Grammar augmentedGrammar = this.grammarAugmentor.augment(grammar);

        Set<GrammarClosureRule> productionSet = new HashSet<GrammarClosureRule>();
        if (augmentedGrammar.getStartSymbol() == null)
        {
            return null;
        }
        Node firstProductionWithDot = this.productionNodeDotRepository.addDotToProductionRightHandSide(augmentedGrammar.getProduction(0));
        productionSet.add(new GrammarClosureRule(firstProductionWithDot));

        LR0ProductionSetDFAState startState = this.compileState(augmentedGrammar, productionSet, false);

        return new LR0ProductionSetDFA(startState, augmentedGrammar);
    }

    private LR0ProductionSetDFAState compileState(Grammar augmentedGrammar, Set<GrammarClosureRule> productionSet, boolean accepting)
    {
        productionSet = this.closureCalculator.calculate(augmentedGrammar, productionSet);
        LR0ProductionSetDFAState startState = new LR0ProductionSetDFAState("", accepting, productionSet);

        LR0ProductionSetDFAState cachedStartState = this.productionSetToStateMap.get(productionSet);
        if (cachedStartState != null)
        {
            return cachedStartState;
        }

        this.productionSetToStateMap.put(productionSet, startState);
        this.compileAndAttachAdjacentStates(augmentedGrammar, startState);

        return startState;
    }

    private void compileAndAttachAdjacentStates(Grammar augmentedGrammar, LR0ProductionSetDFAState state)
    {
        ValueToSetMap<Node, GrammarClosureRule> symbolToNextClosureMap = new ValueToSetMap<Node, GrammarClosureRule>();

        Set<GrammarClosureRule> closureRules = state.getClosureRules();
        for (GrammarClosureRule closureRule: closureRules)
        {
            Node productionNode = closureRule.getProductionNode();
            Node nextSymbol = this.productionNodeDotRepository.findProductionSymbolAfterDot(productionNode);
            if (nextSymbol != null && !(nextSymbol instanceof EpsilonNode))
            {
                Node productionWithDotShifted = this.productionNodeDotRepository.shiftDotInProduction(productionNode);
                symbolToNextClosureMap.put(nextSymbol, new GrammarClosureRule(productionWithDotShifted));
            }
        }

        for (Map.Entry<Node, Set<GrammarClosureRule>> mapEntry: symbolToNextClosureMap.entrySet())
        {
            Node input = mapEntry.getKey();
            Set<GrammarClosureRule> nextStateProductionSet = mapEntry.getValue();

            boolean nextStateAccepting = input instanceof EndOfStringNode;
            LR0ProductionSetDFAState nextState = this.compileState(augmentedGrammar, nextStateProductionSet, nextStateAccepting);
            state.addTransition(new StateTransition<Node, LR0ProductionSetDFAState>(input, nextState));
        }
    }
}
