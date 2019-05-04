package expression.parser;

import exceptions.MyException;
import expression.TripleExpression;
import expression.generic.GenericTabulator;

public class Main {
    public static void main(String[] args) {
        System.out.print("The result is: ");
        GenericTabulator parser = new GenericTabulator();
        Object[][][] matrix;
        try {
            matrix = parser.tabulate("i", "x + y + z", 1, 5, 1, 5, 1, 5);
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j <= 3; j++) {
                    for (int k = 0; k <= 3; k++) {
                        System.out.printf("%d %d %d:", i, j, k);
                        System.out.println(matrix[i][j][k]);
                    }
                }
            }
        } catch (MyException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}