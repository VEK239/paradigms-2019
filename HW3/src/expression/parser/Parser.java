package expression.parser;

import exceptions.MyException;
import expression.TripleExpression;

public interface Parser<T> {
    TripleExpression parse(String expression) throws MyException;
}
