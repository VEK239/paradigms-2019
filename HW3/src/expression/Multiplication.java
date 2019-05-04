package expression;

import exceptions.OverflowException;
import expression.operations.OperationMode;

public class Multiplication<T> extends AbstractBinaryOperator<T> {
    public Multiplication(TripleExpression<T> x, TripleExpression<T> y, OperationMode<T> op) {
        super(x, y, op);
    }

    public T count(T x, T y) throws OverflowException {
        return operationMode.mul(x, y);
    }
}
