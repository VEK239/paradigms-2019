package exceptions;

public class OperationExpectedException extends MyException {
    public OperationExpectedException(final int ind) {
        super("Operation expected at position: " + (ind + 1));
    }
}
