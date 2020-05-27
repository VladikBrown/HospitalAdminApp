package server.exceptions;

public class NoSuchDataBaseException extends Exception {
    public NoSuchDataBaseException() {
    }

    public NoSuchDataBaseException(String message, Throwable exception) {
        super(message, exception);
    }

    public NoSuchDataBaseException(String message) {
        super(message);
    }

    public NoSuchDataBaseException(Throwable exception) {
        super(exception);
    }
}
