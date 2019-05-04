package expression;

import exceptions.OverflowException;
import exceptions.MyException;

public class CheckedSub extends AbstractBinaryOperator {
    public CheckedSub(TripleExpression x, TripleExpression y) {
        super(x, y);
    }

    protected void checker(int x, int y) throws OverflowException {
        if (x >= 0 && y < 0 && Integer.MAX_VALUE + y < x) {
            throw new OverflowException("Max int value");
        }
        if (x < 0 && y > 0 && Integer.MIN_VALUE + y > x) {
            throw new OverflowException("Min int value");
        }
    }

    public int count(int x, int y) throws MyException {
        checker(x, y);
        return x - y;
    }
}
