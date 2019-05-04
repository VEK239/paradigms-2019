package expression;

import exceptions.MyException;

public interface TripleExpression <T> {
    T evaluate(T x, T y, T z) throws MyException;
}
