package expression.operations;

import exceptions.IncorrectNumberException;
import exceptions.ZeroDivException;

public class ShortOperationMode implements OperationMode<Short> {
    @Override
    public Short parseNumber(String number) throws IncorrectNumberException {
        try {
            int tmp = Integer.parseInt(number);
            return (short)tmp;
        } catch (NumberFormatException e) {
            throw new IncorrectNumberException();
        }
    }

    @Override
    public Short add(Short x, Short y) {
        return (short)(x + y);
    }

    @Override
    public Short sub(Short x, Short y) {
        return (short)(x - y);
    }

    @Override
    public Short divide(Short x, Short y) throws ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        return (short)(x / y);
    }

    @Override
    public Short mul(Short x, Short y) {
        return (short)(x * y);
    }

    @Override
    public Short not(Short x) {
        return (short)(-x);
    }

    @Override
    public Short mod(Short x, Short y) throws ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        return (short)(x % y);
    }

    @Override
    public Short square(Short x) {
        return (short)(x * x);
    }

    @Override
    public Short abs(Short x) {
        if (x >= 0) {
            return x;
        } else {
            return (short)(-x);
        }
    }
}
