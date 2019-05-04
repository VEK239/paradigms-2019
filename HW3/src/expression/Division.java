package expression;

import exceptions.OverflowException;
import exceptions.ZeroDivException;
import expression.operations.OperationMode;

public class Division<T> extends AbstractBinaryOperator<T> {
    public Division(TripleExpression<T> x, TripleExpression<T> y, OperationMode<T> op) {
        super(x, y, op);
    }

    public T count(T x, T y) throws OverflowException, ZeroDivException {
        return operationMode.divide(x, y);
    }
}
