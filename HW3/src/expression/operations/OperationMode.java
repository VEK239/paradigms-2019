package expression.operations;

import exceptions.IncorrectNumberException;
import exceptions.OverflowException;
import exceptions.ZeroDivException;

public interface OperationMode<T> {
    T parseNumber(final String number) throws IncorrectNumberException;
    T add(T x, T y) throws OverflowException;
    T sub(T x, T y) throws OverflowException;
    T divide(T x, T y) throws OverflowException, ZeroDivException;
    T mul(T x, T y) throws OverflowException;
    T not(T x) throws OverflowException;
    T mod(T x, T y) throws ZeroDivException;
    T square(T x) throws OverflowException;
    T abs(T x) throws OverflowException;
}
