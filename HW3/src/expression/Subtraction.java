package expression;

import exceptions.OverflowException;
import expression.operations.OperationMode;

public class Subtraction<T> extends AbstractBinaryOperator<T> {
    public Subtraction(TripleExpression<T> x, TripleExpression<T> y, OperationMode<T> op) {
        super(x, y, op);
    }

    public T count(T x, T y) throws OverflowException {
        return operationMode.sub(x, y);
    }
}
