import static org.junit.Assert.assertEquals;
import org.junit.Test;

import larp.automaton.EpsilonNFA;
import larp.automaton.EpsilonNFAState;
import larp.automaton.RegularExpressionSyntaxCompiler;
import larp.automaton.StateTransition;
import larp.grammar.CharacterNode;
import larp.grammar.KleeneClosureNode;

public class RegularExpressionSyntaxCompilerTest
{
    @Test
    public void testCompileReturnsEpsilonNFAForCharacterNode()
    {
        RegularExpressionSyntaxCompiler compiler = new RegularExpressionSyntaxCompiler();
        CharacterNode rootNode = new CharacterNode('a');

        EpsilonNFAState state = new EpsilonNFAState("S0", false);
        state.addTransition(new StateTransition('a', new EpsilonNFAState("S1", false)));
        EpsilonNFA expectedEpsilonNFA = new EpsilonNFA(state);

        assertEquals(expectedEpsilonNFA, compiler.compile(rootNode));
    }

    @Test
    public void testCompilerReturnsEpsilonNFAForKleeneClosureNode()
    {
        RegularExpressionSyntaxCompiler compiler = new RegularExpressionSyntaxCompiler();
        KleeneClosureNode rootNode = new KleeneClosureNode();
        rootNode.addChild(new CharacterNode('a'));

        EpsilonNFAState state1 = new EpsilonNFAState("S0", false);
        EpsilonNFAState state2 = new EpsilonNFAState("S1", false);
        state1.addTransition(new StateTransition('a', state2));
        state1.addTransition(new StateTransition(null, state2));
        state2.addTransition(new StateTransition(null, state1));
        EpsilonNFA expectedEpsilonNFA = new EpsilonNFA(state1);

        assertEquals(expectedEpsilonNFA, compiler.compile(rootNode));
    }
}
