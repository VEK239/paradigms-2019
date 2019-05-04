package exceptions;

public class UnknownModeException extends MyException {
    public UnknownModeException(String mode) {
        super("Unknown mode: " + mode);
    }
}
