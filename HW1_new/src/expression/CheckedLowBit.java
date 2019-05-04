package expression;

public class CheckedLowBit extends AbstractUnaryOperator {
    public CheckedLowBit(TripleExpression x) {
        super(x);
    }

    protected void checker(int x) {
    }

    protected int count(int x) {
        return Integer.lowestOneBit(x);
    }
}
