package expression.operations;

import exceptions.IncorrectNumberException;
import exceptions.ZeroDivException;

public class UncheckedIntegerOperationMode implements OperationMode<Integer> {
    @Override
    public Integer parseNumber(String number) throws IncorrectNumberException {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IncorrectNumberException();
        }
    }

    @Override
    public Integer add(Integer x, Integer y) {
        return x + y;
    }

    @Override
    public Integer sub(Integer x, Integer y) {
        return x - y;
    }

    @Override
    public Integer divide(Integer x, Integer y) throws ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        return x / y;
    }

    @Override
    public Integer mul(Integer x, Integer y) {
        return x * y;
    }

    @Override
    public Integer not(Integer x) {
        return -x;
    }

    @Override
    public Integer mod(Integer x, Integer y) throws ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        return x % y;
    }

    @Override
    public Integer square(Integer x) {
        return x * x;
    }

    @Override
    public Integer abs(Integer x) {
        if (x >= 0) {
            return x;
        } else {
            return -x;
        }
    }
}
