package expression;
import exceptions.MyException;
import expression.operations.OperationMode;

public abstract class AbstractBinaryOperator<T> implements TripleExpression<T> {
    final private TripleExpression<T> firstMember;
    final private TripleExpression<T> secondMember;
    final OperationMode<T> operationMode;

    AbstractBinaryOperator(TripleExpression<T> x, TripleExpression<T> y, OperationMode<T> op) {
        firstMember = x;
        secondMember = y;
        operationMode = op;
    }

    public T evaluate(T x, T y, T z) throws MyException {
        return count(firstMember.evaluate(x, y, z), secondMember.evaluate(x, y, z));
    }

    protected abstract T count(T x, T y) throws MyException;
}
