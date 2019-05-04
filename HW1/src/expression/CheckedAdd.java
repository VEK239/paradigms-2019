package expression;

import exceptions.OverflowException;
import exceptions.MyException;

public class CheckedAdd extends AbstractBinaryOperator {
    public CheckedAdd(TripleExpression x, TripleExpression y) {
        super(x, y);
    }

    protected void checker(int x, int y) throws OverflowException {
        if (x > 0 && Integer.MAX_VALUE - x < y) {
            throw new OverflowException("Max int overflow");
        }
        if (x < 0 && Integer.MIN_VALUE - x > y) {
            throw new OverflowException("Min int overflow");
        }
    }

    public int count(int x, int y) throws MyException {
        checker(x, y);
        return x + y;
    }
}
