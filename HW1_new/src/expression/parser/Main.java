package expression.parser;

import exceptions.MyException;
import expression.TripleExpression;

public class Main {
    public static void main(String[] args) {
        System.out.print("The result is: ");
        ExpressionParser parser = new ExpressionParser();
        try {
            System.out.println(parser.parse("- high -1139330527 / (low y)").evaluate(2, 2, 2));
        } catch (MyException ex) {
            System.out.println(ex.getMessage());
        }

    }
}