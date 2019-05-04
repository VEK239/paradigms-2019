package expression;

import exceptions.OverflowException;

public class CheckedHighBit extends AbstractUnaryOperator {
    public CheckedHighBit(TripleExpression x) {
        super(x);
    }

    protected void checker(int x) {
    }

    protected int count(int x) throws OverflowException {
        return Integer.highestOneBit(x);
    }
}
