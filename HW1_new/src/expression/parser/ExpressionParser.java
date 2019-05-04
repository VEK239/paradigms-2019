package expression.parser;

import exceptions.*;
import expression.*;

public class ExpressionParser implements Parser {
    private String expression;
    private TokenValue currentToken;
    private int currentIndex;
    private int operandExpectedIndex;
    private int value;
    private char var;
    private String funcIdentifier;
    private int leftBound;

    public TripleExpression parse(String line) throws MyException {
        expression = line;
        currentToken = TokenValue.BEGIN;
        currentIndex = 0;
        TripleExpression result = addSub();
        if (currentToken != TokenValue.END) {
            throw new EOFException("Expected EOF at position: " + currentIndex);
        }
        return result;
    }

    private void longOperation() {
        leftBound = currentIndex - 1;
        while (currentIndex < expression.length() && Character.isLetter(expression.charAt(currentIndex))) {
            currentIndex++;
        }
        funcIdentifier = expression.substring(leftBound, currentIndex);
    }

    private void numberGetter() throws OverflowException {
        int leftBound = currentIndex - 1;
        while (currentIndex < expression.length() && Character.isDigit(expression.charAt(currentIndex))) {
            currentIndex++;
        }
        try {
            value = Integer.parseInt(expression.substring(leftBound, currentIndex));
            currentIndex--;
        } catch (NumberFormatException e) {
            throw new OverflowException((expression.charAt(leftBound) == '-' ? "Negative" : "Positive")
                    + " overflow at position: " + (leftBound + 1));
        }
        currentIndex++;
    }

    private TripleExpression addSub() throws MyException {
        TripleExpression result = mulDiv();
        while (true) {
            switch (currentToken) {
                case SUB:
                    result = new CheckedSub(result, mulDiv());
                    break;
                case ADD:
                    result = new CheckedAdd(result, mulDiv());
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression mulDiv() throws MyException {
        TripleExpression result = unary();
        while (true) {
            switch (currentToken) {
                case MUL:
                    result = new CheckedMul(result, unary());
                    break;
                case DIV:
                    result = new CheckedDivide(result, unary());
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression unary() throws MyException {
        TripleExpression result;
        switch (nextToken()) {
            case LEFT_BRACKET:
                result = addSub();
                if (currentToken != TokenValue.RIGHT_BRACKET) {
                    throw new WrongBracesException("Expected closing bracket at position: " + currentIndex);
                }
                nextToken();
                break;
            case NUMBER:
                numberGetter();
                result = new Const(value);
                nextToken();
                break;
            case VARIABLE:
                result = new Variable(var);
                nextToken();
                break;
            case NEG:
                result = new CheckedNot(unary());
                break;
            case FUNC:
                longOperation();
                if (funcIdentifier.equals("high")) {
                    result = new CheckedHighBit(unary());
                } else if (funcIdentifier.equals("low")) {
                    result = new CheckedLowBit(unary());
                } else {
                    throw new UnknownIdentifierException(funcIdentifier, leftBound);
                }
                break;
            default:
                throw new OperandExpectedException(operandExpectedIndex);
        }
        return result;
    }

    private TokenValue nextToken() {
        char currentSymbol;
        TokenValue prevToken = currentToken;
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
            case ('*'):
                operandExpectedIndex = currentIndex;
                currentToken = TokenValue.MUL;
                break;
            case ('/'):
                operandExpectedIndex = currentIndex;
                currentToken = TokenValue.DIV;
                break;
            case '-':
                if (prevToken == TokenValue.VARIABLE || prevToken == TokenValue.RIGHT_BRACKET
                    || prevToken == TokenValue.NUMBER) {
                    currentToken = TokenValue.SUB;
                } else if (expression.length() > currentIndex + 1 &&
                        Character.isDigit(expression.charAt(currentIndex + 1))) {
                    currentToken = TokenValue.NUMBER;
                } else {
                    currentToken = TokenValue.NEG;
                }
                break;
            case ('('):
                currentToken = TokenValue.LEFT_BRACKET;
                break;
            case (')'):
                operandExpectedIndex = currentIndex;
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
                    currentToken = TokenValue.FUNC;
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
    FUNC
}