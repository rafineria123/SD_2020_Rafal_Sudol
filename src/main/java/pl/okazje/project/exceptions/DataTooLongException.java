package pl.okazje.project.exceptions;

public class DataTooLongException extends RuntimeException {

    public DataTooLongException(String message) {
        super(message);
    }

    public DataTooLongException(String message, Throwable ex) {
        super(message, ex);
    }

}
