package expression.operations;

import exceptions.IncorrectNumberException;
import exceptions.ZeroDivException;

public class LongOperationMode implements OperationMode<Long> {
    @Override
    public Long parseNumber(String number) throws IncorrectNumberException {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new IncorrectNumberException();
        }
    }

    @Override
    public Long add(Long x, Long y) {
        return x + y;
    }

    @Override
    public Long sub(Long x, Long y) {
        return x - y;
    }

    @Override
    public Long divide(Long x, Long y) throws ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        return x / y;
    }

    @Override
    public Long mul(Long x, Long y) {
        return x * y;
    }

    @Override
    public Long not(Long x) {
        return -x;
    }

    @Override
    public Long mod(Long x, Long y) throws ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        return null;
    }

    @Override
    public Long square(Long x) {
        return x * x;
    }

    @Override
    public Long abs(Long x) {
        if (x >= 0) {
            return x;
        } else {
            return -x;
        }
    }
}
