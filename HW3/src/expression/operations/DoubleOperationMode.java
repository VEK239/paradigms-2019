package expression.operations;

import exceptions.IncorrectNumberException;

public class DoubleOperationMode implements OperationMode<Double> {
    @Override
    public Double parseNumber(String number) throws IncorrectNumberException {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            throw new IncorrectNumberException();
        }
    }

    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double sub(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double divide(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double mul(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double not(Double x) {
        return -x;
    }

    @Override
    public Double mod(Double x, Double y) {
        return x % y;
    }

    @Override
    public Double square(Double x) {
        return x * x;
    }

    @Override
    public Double abs(Double x) {
        if (x >= 0) {
            return x;
        } else {
            return -x;
        }
    }
}
