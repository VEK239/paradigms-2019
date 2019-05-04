package expression.operations;

import exceptions.IncorrectNumberException;
import exceptions.OverflowException;
import exceptions.ZeroDivException;

import java.math.BigInteger;

public class BigIntegerOperationMode implements OperationMode<BigInteger> {
    @Override
    public BigInteger parseNumber(String number) throws IncorrectNumberException {
        try {
            return new BigInteger(number);
        } catch (NumberFormatException e) {
            throw new IncorrectNumberException();
        }
    }

    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger sub(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) throws ZeroDivException {
        if (y.equals(BigInteger.ZERO)) {
            throw new ZeroDivException();
        }
        return x.divide(y);
    }

    @Override
    public BigInteger mul(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger not(BigInteger x) {
        return x.not();
    }

    @Override
    public BigInteger mod(BigInteger x, BigInteger y) throws ZeroDivException {
        if (y.signum() != 1) {
            throw new ZeroDivException();
        }
        return x.mod(y);
    }

    @Override
    public BigInteger square(BigInteger x) {
        return x.multiply(x);
    }

    @Override
    public BigInteger abs(BigInteger x) {
        return x.abs();
    }
}
