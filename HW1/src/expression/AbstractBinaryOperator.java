package expression;

import exceptions.MyException;

public abstract class AbstractBinaryOperator implements TripleExpression {
    final private TripleExpression firstMember;
    final private TripleExpression secondMember;

    public AbstractBinaryOperator(TripleExpression x, TripleExpression y) {
        firstMember = x;
        secondMember = y;
    }

    public int evaluate(int x, int y, int z) throws MyException {
        return count(firstMember.evaluate(x, y, z), secondMember.evaluate(x, y, z));
    }

    protected abstract int count(int x, int y) throws MyException;

    protected abstract void checker(int x, int y) throws MyException;
}
