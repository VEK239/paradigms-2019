package expression.operations;

import exceptions.IncorrectNumberException;
import exceptions.OverflowException;
import exceptions.ZeroDivException;

public class IntegerOperationMode implements OperationMode<Integer> {
    @Override
    public Integer parseNumber(String number) throws IncorrectNumberException {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IncorrectNumberException();
        }
    }

    @Override
    public Integer add(Integer x, Integer y) throws OverflowException {
        if (x > 0 && Integer.MAX_VALUE - x < y) {
            throw new OverflowException("Max int overflow");
        } else if (x < 0 && Integer.MIN_VALUE - x > y) {
            throw new OverflowException("Min int overflow");
        }
        return x + y;
    }

    @Override
    public Integer sub(Integer x, Integer y) throws OverflowException {
        if (x >= 0 && y < 0 && Integer.MAX_VALUE + y < x) {
            throw new OverflowException("Max int value");
        } else if (x < 0 && y > 0 && Integer.MIN_VALUE + y > x) {
            throw new OverflowException("Min int value");
        }
        return x - y;
    }

    @Override
    public Integer divide(Integer x, Integer y) throws OverflowException, ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        } else if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("Max int overflow");
        }
        return x / y;
    }

    @Override
    public Integer mul(Integer x, Integer y) throws OverflowException {
        if (x > 0 && y > 0 && Integer.MAX_VALUE / x < y) {
            throw new OverflowException("Max int overflow");
        } else if (x > 0 && y < 0 && Integer.MIN_VALUE / x > y) {
            throw new OverflowException("Min int overflow");
        } else if (x < 0 && y > 0 && Integer.MIN_VALUE / y > x) {
            throw new OverflowException("Min int overflow");
        } else if (x < 0 && y < 0 && Integer.MAX_VALUE / x > y) {
            throw new OverflowException("Max int overflow");
        }
        return x * y;
    }

    @Override
    public Integer not(Integer x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Max int overflow");
        }
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
    public Integer square(Integer x) throws OverflowException {
        if (x <= Integer.MIN_VALUE / 2 || x > Integer.MAX_VALUE / 2) {
            throw new OverflowException("Max int overflow");
        }
        return x * x;
    }

    @Override
    public Integer abs(Integer x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Max int overflow");
        }
        if (x >= 0) {
            return x;
        } else {
            return -x;
        }
    }
}
