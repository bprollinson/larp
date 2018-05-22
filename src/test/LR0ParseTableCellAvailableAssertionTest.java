import org.junit.Test;
import static org.junit.Assert.assertEquals;

import larp.parser.contextfreelanguage.AmbiguousLR0ParseTableException;

public class LR0ParseTableCellAvailableAssertionTest
{
    @Test(expected = AmbiguousLR0ParseTableException.class)
    public void testValidateThrowsExceptionForShiftActionWhenCellContainsExistingShiftAction()
    {
    }

    @Test
    public void testValidateDoesNotThrowExceptionForShiftActionWhenCellContainsTheSameStateAndDifferentSymbol()
    {
        assertEquals(0, 1);
    }
}
