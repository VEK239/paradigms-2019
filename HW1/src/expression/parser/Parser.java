package expression.parser;

import exceptions.MyException;
import expression.TripleExpression;

public interface Parser {
    TripleExpression parse(String expression) throws MyException;
}
