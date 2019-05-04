package exceptions;

public class OperandExpectedException extends MyException {
    public OperandExpectedException(final int ind) {
        super("Operand expected at position: " + (ind + 1));
    }
}
