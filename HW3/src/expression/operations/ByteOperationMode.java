package expression.operations;

import exceptions.IncorrectNumberException;
import exceptions.OverflowException;
import exceptions.ZeroDivException;

public class ByteOperationMode implements OperationMode<Byte> {

    @Override
    public Byte parseNumber(String number) throws IncorrectNumberException {
        try {
            int tmp = Integer.parseInt(number);
            return (byte)tmp;
        } catch (NumberFormatException e) {
            throw new IncorrectNumberException();
        }
    }

    @Override
    public Byte add(Byte x, Byte y) {
        return (byte)(x + y);
    }

    @Override
    public Byte sub(Byte x, Byte y) {
        return (byte)(x - y);
    }

    @Override
    public Byte divide(Byte x, Byte y) throws ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        return (byte)(x / y);
    }

    @Override
    public Byte mul(Byte x, Byte y) {
        return (byte)(x * y);
    }

    @Override
    public Byte not(Byte x) {
        return (byte)(-x);
    }

    @Override
    public Byte mod(Byte x, Byte y) throws ZeroDivException {
        if (y == 0) {
            throw new ZeroDivException();
        }
        return (byte)(x % y);
    }

    @Override
    public Byte square(Byte x) {
        return (byte)(x * x);
    }

    @Override
    public Byte abs(Byte x) {
        if (x >= 0) {
            return x;
        } else {
            return (byte)(-x);
        }
    }
}
