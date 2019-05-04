package expression;

import exceptions.OverflowException;

public class CheckedNot extends AbstractUnaryOperator {
    public CheckedNot(TripleExpression x) {
        super(x);
    }

    protected void checker(int x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Max int overflow");
        }
    }

    protected int count(int x) throws OverflowException {
        checker(x);
        return -x;
    }
}
