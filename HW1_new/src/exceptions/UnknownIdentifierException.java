package exceptions;

public class UnknownIdentifierException extends MyException {
    public UnknownIdentifierException(final String id, final int ind) {
        super("Unknown identifier \"" + id + "\" at position: " + (ind + 1));
    }
}
