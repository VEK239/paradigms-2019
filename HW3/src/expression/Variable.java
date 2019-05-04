package expression;

public class Variable<T> implements TripleExpression<T> {
    private final char var;

    public Variable(char x) {
        var = x;
    }

    public Variable(String x) {
        var = x.charAt(0);
    }

    public T evaluate(T x, T y, T z) {
        switch (var) {
            case 'x':
                return x;
            case 'y':
                return y;
            case 'z':
                return z;
            default:
                return null;
        }
    }
}
