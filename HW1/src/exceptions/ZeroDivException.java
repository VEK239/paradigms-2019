package exceptions;

public class ZeroDivException extends MyException {
    public ZeroDivException() {
        super("division by zero");
    }
}
