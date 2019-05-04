package expression.parser;

import exceptions.*;
import expression.*;
import expression.operations.OperationMode;

import java.util.Map;

public class ExpressionParser<T> implements Parser<T> {
    private String expression;
    private TokenValue currentToken;
    private TokenValue prevToken;
    private int currentIndex;
    private int operandExpectedIndex;
    private T value;
    private char var;
    private int bracesCount;
    private OperationMode<T> operationMode;

    public ExpressionParser(OperationMode<T> mode) {
        operationMode = mode;
    }

    public TripleExpression<T> parse(String line) throws MyException {
        expression = line;
        currentToken = TokenValue.BEGIN;
        currentIndex = 0;
        bracesCount = 0;
        return addSub();
    }

    private void longOperation(char currentSymbol) {
        Map<String, Integer> key = Map.of(
                "key", 10,
                "zzz", 20
        );
        String function;
        switch (currentSymbol) {
            case 'm':
                function = "mod";
                currentToken = TokenValue.MOD;
                break;
            case 's':
                function = "square";
                currentToken = TokenValue.SQUARE;
                break;
            case 'a':
                function = "abs";
                currentToken = TokenValue.ABS;
                break;
            default:
                currentToken = TokenValue.MISTAKE;
                return;
        }
        if (currentIndex + function.length() <= expression.length() &&
                function.equals(expression.substring(currentIndex, currentIndex + function.length())) &&
                expression.length() > currentIndex + function.length() &&
                !Character.isLetter(expression.charAt(currentIndex + function.length()))) {
            currentIndex += function.length() - 1;
            operandExpectedIndex = currentIndex;
        } else {
            currentToken = TokenValue.MISTAKE;
        }
    }

    private boolean commonOpChecker() {
        return prevToken == TokenValue.VARIABLE || prevToken == TokenValue.RIGHT_BRACKET
                || prevToken == TokenValue.NUMBER;
    }

    private void numberGetter(int leftBound) throws OverflowException, IncorrectNumberException {
        while (currentIndex < expression.length() && Character.isDigit(expression.charAt(currentIndex))) {
            currentIndex++;
        }
        try {
            value = operationMode.parseNumber(expression.substring(leftBound, currentIndex));
            currentIndex--;
        } catch (NumberFormatException e) {
            throw new OverflowException((expression.charAt(leftBound) == '-' ? "Negative" : "Positive")
                    + " overflow at position: " + (leftBound + 1));
        }
        currentIndex++;
    }

    private void operationExpectedThrower() throws OperationExpectedException {
        if (commonOpChecker()) {
            throw new OperationExpectedException(currentIndex);
        }
    }

    private void unknownIdentifierThrower() throws UnknownIdentifierException {
        int leftBound = currentIndex - 1;
        while (currentIndex < expression.length() && Character.isLetter(expression.charAt(currentIndex))) {
            currentIndex++;
        }
        throw new UnknownIdentifierException(expression.substring(leftBound, currentIndex), leftBound);
    }

    private void operandExpectedThrower(final int excIndex) throws OperandExpectedException {
        if (prevToken == TokenValue.BEGIN || !commonOpChecker())
            throw new OperandExpectedException(excIndex);
    }

    private void wrongBracesThrower() throws WrongBracesException {
        String s;
        if (bracesCount < 0 && currentToken == TokenValue.RIGHT_BRACKET) {
            s = "opening";
        } else if (bracesCount > 0 && currentToken == TokenValue.END) {
            s = "closing";
        } else {
            return;
        }
        throw new WrongBracesException("Odd " + s + " bracket at position: " + currentIndex);
    }

    private TripleExpression<T> addSub() throws MyException {
        TripleExpression<T> result = mulDiv();
        while (true) {
            switch (currentToken) {
                case SUB:
                    result = new Subtraction<>(result, mulDiv(), operationMode);
                    break;
                case ADD:
                    operandExpectedThrower(operandExpectedIndex);
                    result = new Addition<>(result, mulDiv(), operationMode);
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression<T> mulDiv() throws MyException {
        TripleExpression<T> result = unary();
        while (true) {
            switch (currentToken) {
                case MOD:
                    operandExpectedThrower(operandExpectedIndex);
                    result = new Mod<>(result, unary(), operationMode);
                    break;
                case MUL:
                    operandExpectedThrower(operandExpectedIndex);
                    result = new Multiplication<>(result, unary(), operationMode);
                    break;
                case DIV:
                    operandExpectedThrower(operandExpectedIndex);
                    result = new Division<>(result, unary(), operationMode);
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression<T> unary() throws MyException {
        TripleExpression<T> result;
        switch (nextToken()) {
            case LEFT_BRACKET:
                operationExpectedThrower();
                result = addSub();
                unary();
                break;
            case NUMBER:
                numberGetter(currentIndex - 1);
                operationExpectedThrower();
                result = new Const<>(value);
                unary();
                break;
            case VARIABLE:
                operationExpectedThrower();
                result = new Variable<>(var);
                unary();
                break;
            case NEG:
                result = new Not<>(unary(), operationMode);
                break;
            case ABS:
                operationExpectedThrower();
                result = new Abs<>(unary(), operationMode);
                break;
            case SQUARE:
                operationExpectedThrower();
                result = new Square<>(unary(), operationMode);
                break;
            case RIGHT_BRACKET:
            case END:
                operandExpectedThrower(operandExpectedIndex);
                wrongBracesThrower();
                return new Const<>(null);
            case MISTAKE:
                unknownIdentifierThrower();
            default:
                return new Const<>(null);
        }
        return result;
    }

    private TokenValue nextToken() {
        char currentSymbol;
        prevToken = currentToken;
        skipSpace();
        if (currentIndex >= expression.length()) {
            currentToken = TokenValue.END;
            return TokenValue.END;
        }
        currentSymbol = expression.charAt(currentIndex);
        switch (currentSymbol) {
            case '+':
                operandExpectedIndex = currentIndex;
                currentToken = TokenValue.ADD;
                break;
            case '-':
                if (commonOpChecker()) {
                    currentToken = TokenValue.SUB;
                } else if (expression.length() > currentIndex + 1 &&
                        Character.isDigit(expression.charAt(currentIndex + 1))) {
                    currentToken = TokenValue.NUMBER;
                } else {
                    currentToken = TokenValue.NEG;
                }
                break;
            case ('*'):
                operandExpectedIndex = currentIndex;
                currentToken = TokenValue.MUL;
                break;
            case ('/'):
                operandExpectedIndex = currentIndex;
                currentToken = TokenValue.DIV;
                break;
            case ('('):
                currentToken = TokenValue.LEFT_BRACKET;
                bracesCount++;
                break;
            case (')'):
                operandExpectedIndex = currentIndex;
                bracesCount--;
                currentToken = TokenValue.RIGHT_BRACKET;
                break;
            case 'x':
            case 'y':
            case 'z':
                var = currentSymbol;
                currentToken = TokenValue.VARIABLE;
                break;
            default:
                if (Character.isDigit(currentSymbol)) {
                    currentToken = TokenValue.NUMBER;
                } else {
                    longOperation(currentSymbol);
                }
        }
        currentIndex++;
        return currentToken;
    }

    private void skipSpace() {
        while (currentIndex < expression.length() && Character.isWhitespace(expression.charAt(currentIndex))) {
            currentIndex++;
        }
    }
}

enum TokenValue {
    NUMBER,
    VARIABLE,
    NEG,
    ADD,
    SUB,
    MUL,
    DIV,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    BEGIN,
    END,
    MISTAKE,
    SQUARE,
    MOD,
    ABS
}