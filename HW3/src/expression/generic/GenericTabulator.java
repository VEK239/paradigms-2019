package expression.generic;

import exceptions.MyException;
import exceptions.UnknownModeException;
import expression.TripleExpression;
import expression.operations.*;
import expression.parser.ExpressionParser;

public class GenericTabulator implements Tabulator {

    private <T> Object[][][] parseTabulator(OperationMode<T> mode, String expression, int x1, int x2, int y1,
                                            int y2, int z1, int z2) {
        Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        try {
            TripleExpression <T> currentExpression;
            currentExpression = new ExpressionParser<>(mode).parse(expression);
            for (int i = x1; i <= x2; i++) {
                for (int j = y1; j <= y2; j++) {
                    for (int k = z1; k <= z2; k++) {
                        try {
                            res[i - x1][j - y1][k - z1] = currentExpression.evaluate(mode.parseNumber(Integer.toString(i)),
                                    mode.parseNumber(Integer.toString(j)), mode.parseNumber(Integer.toString(k)));
                        } catch (MyException e) {
                            res[i - x1][j - y1][k - z1] = null;
                        }
                    }
                }
            }
            return res;
        } catch (MyException e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return parseTabulator(getOperationMode(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private OperationMode<? extends Number> getOperationMode(String mode) throws UnknownModeException {
        switch (mode) {
            case "s":
                return new ShortOperationMode();
            case "l":
                return new LongOperationMode();
            case "u":
                return new UncheckedIntegerOperationMode();
            case "i":
                return new IntegerOperationMode();
            case "f":
                return new FloatOperationMode();
            case "b":
                return new ByteOperationMode();
            case "bi":
                return new BigIntegerOperationMode();
            case "d":
                return new DoubleOperationMode();
            default:
                throw new UnknownModeException(mode);
        }
    }
}
