package expression;

import exceptions.MyException;
import exceptions.OverflowException;
import exceptions.ZeroDivException;
import expression.operations.OperationMode;

public class Mod<T> extends AbstractBinaryOperator<T> {
    public Mod(TripleExpression<T> x, TripleExpression<T> y, OperationMode<T> op) {
        super(x, y, op);
    }

    public T count(T x, T y) throws ZeroDivException {
        return operationMode.mod(x, y);
    }
}
