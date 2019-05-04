package expression;

import exceptions.MyException;
import exceptions.OverflowException;
import expression.operations.OperationMode;

public class Abs<T> extends AbstractUnaryOperator<T> {
    public Abs(TripleExpression<T> x, OperationMode<T> op) {
        super(x, op);
    }

    protected T count(T x) throws OverflowException {
        return operationMode.abs(x);
    }
}
