package expression;

import exceptions.MyException;
import expression.operations.OperationMode;

public abstract class AbstractUnaryOperator<T> implements TripleExpression<T> {
    final private TripleExpression<T> member;
    final OperationMode<T> operationMode;

    AbstractUnaryOperator(TripleExpression<T> x, OperationMode<T> op) {
        member = x;
        operationMode = op;
    }

    public T evaluate(T x, T y, T z) throws MyException {
        return count(member.evaluate(x, y, z));
    }

    protected abstract T count(T x) throws MyException;
}
