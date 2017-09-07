import larp.grammar.RegularExpressionSyntaxParser;
import larp.statemachine.State;
import larp.statemachine.StateMachine;
import larp.statemachine.StateTransition;

public class LARP
{
    public static void main(String args[])
    {
        System.out.println("LARP main");

        State state0 = new State("S0", false);
        State state1 = new State("S1", true);
        state0.addTransition(new StateTransition('a', state1));

        StateMachine machine = new StateMachine(state0);
        System.out.println(": " + machine.accepts(""));
        System.out.println("a: " + machine.accepts("a"));
        System.out.println("b: " + machine.accepts("b"));

        RegularExpressionSyntaxParser parser = new RegularExpressionSyntaxParser();
        try
        {
            parser.parse("test");
            System.out.println("Success");
        }
        catch (Exception e)
        {
            System.out.println("Failure");
        }

        try
        {
            parser.parse("(");
            System.out.println("Failure");
        }
        catch (Exception e)
        {
            System.out.println("Success");
        }

        try
        {
            parser.parse(")(");
            System.out.println("Failure");
        }
        catch (Exception e)
        {
            System.out.println("Success");
        }

        try
        {
            parser.parse("a*");
            System.out.println("Success");
        }
        catch (Exception e)
        {
            System.out.println("Failure");
        }

        try
        {
            parser.parse("(a)*");
            System.out.println("Success");
        }
        catch (Exception e)
        {
            System.out.println("Failure");
        }

        try
        {
            parser.parse("(*)");
            System.out.println("Failure");
        }
        catch (Exception e)
        {
            System.out.println("Success");
        }

        try
        {
            parser.parse("*");
            System.out.println("Failure");
        }
        catch (Exception e)
        {
            System.out.println("Success");
        }

        try
        {
            parser.parse("(|)");
            System.out.println("Failure");
        }
        catch (Exception e)
        {
            System.out.println("Success");
        }
    }
}
