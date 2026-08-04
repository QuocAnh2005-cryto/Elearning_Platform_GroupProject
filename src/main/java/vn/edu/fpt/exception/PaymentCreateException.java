package vn.edu.fpt.exception;

public class PaymentCreateException extends RuntimeException {

    public PaymentCreateException(String message) {
        super(message);
    }

    public PaymentCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
