package server.exceptions;

public class CollectionNotFoundException extends Exception {
    public CollectionNotFoundException() {
    }

    public CollectionNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public CollectionNotFoundException(String message) {
        super(message);
    }

    public CollectionNotFoundException(Throwable exception) {
        super(exception);
    }
}
