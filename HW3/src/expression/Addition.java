package expression;

import exceptions.OverflowException;
import expression.operations.OperationMode;

public class Addition<T> extends AbstractBinaryOperator<T> {
    public Addition(TripleExpression<T> x, TripleExpression<T> y, OperationMode<T> op) {
        super(x, y, op);
    }

    public T count(T x, T y) throws OverflowException {
        return operationMode.add(x, y);
    }
}
