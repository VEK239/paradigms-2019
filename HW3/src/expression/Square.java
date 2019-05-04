package expression;

import exceptions.MyException;
import exceptions.OverflowException;
import expression.operations.OperationMode;

public class Square<T> extends AbstractUnaryOperator<T> {
    public Square(TripleExpression<T> x, OperationMode<T> op) {
        super(x, op);
    }

    protected T count(T x) throws OverflowException {
        return operationMode.square(x);
    }
}
