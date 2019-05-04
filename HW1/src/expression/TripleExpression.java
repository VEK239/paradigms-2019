package expression;

import exceptions.MyException;

public interface TripleExpression {
    int evaluate(int x, int y, int z) throws MyException;
}
