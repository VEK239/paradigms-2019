package expression.operations;

import exceptions.IncorrectNumberException;

public class FloatOperationMode implements OperationMode<Float> {
    @Override
    public Float parseNumber(String number) throws IncorrectNumberException {
        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            throw new IncorrectNumberException();
        }
    }

    @Override
    public Float add(Float x, Float y) {
        return x + y;
    }

    @Override
    public Float sub(Float x, Float y) {
        return x - y;
    }

    @Override
    public Float divide(Float x, Float y) {
        return x / y;
    }

    @Override
    public Float mul(Float x, Float y) {
        return x * y;
    }

    @Override
    public Float not(Float x) {
        return -x;
    }

    @Override
    public Float mod(Float x, Float y) {
        return x % y;
    }

    @Override
    public Float square(Float x) {
        return x * x;
    }

    @Override
    public Float abs(Float x) {
        if (x >= 0) {
            return x;
        } else {
            return -x;
        }
    }
}
