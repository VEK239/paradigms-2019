package expression;

import exceptions.OverflowException;
import expression.operations.OperationMode;

public class Not<T> extends AbstractUnaryOperator<T> {
    public Not(TripleExpression<T> x, OperationMode<T> op) {
        super(x, op);
    }

    protected T count(T x) throws OverflowException {
        return operationMode.not(x);
    }
}
