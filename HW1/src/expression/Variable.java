package expression;

public class Variable implements TripleExpression {
    private final char var;

    public Variable(char x) {
        var = x;
    }

    public Variable(String x) {
        var = x.charAt(0);
    }

    public int evaluate(int x, int y, int z) {
        switch (var) {
            case 'x':
                return x;
            case 'y':
                return y;
            case 'z':
                return z;
            default:
                return 0;
        }
    }
}
