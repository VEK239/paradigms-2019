package expression;

import exceptions.OverflowException;
import exceptions.ZeroDivException;
import exceptions.MyException;

public class CheckedDivide extends AbstractBinaryOperator {
    public CheckedDivide(TripleExpression x, TripleExpression y) {
        super(x, y);
    }

    protected void checker(int x, int y) throws ZeroDivException, OverflowException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("Max int overflow");
        }
    }

    public int count(int x, int y) throws MyException {
        checker(x, y);
        return x / y;
    }
}
