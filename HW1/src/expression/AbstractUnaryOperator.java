package expression;

import exceptions.MyException;

public abstract class AbstractUnaryOperator implements TripleExpression {
    final private TripleExpression Member;

    public AbstractUnaryOperator(TripleExpression x) {
        Member = x;
    }

    public int evaluate(int x, int y, int z) throws MyException {
        return count(Member.evaluate(x, y, z));
    }

    protected abstract int count(int x) throws MyException;

    protected abstract void checker(int x) throws MyException;
}
