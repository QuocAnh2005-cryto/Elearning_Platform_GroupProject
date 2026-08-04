package vn.edu.fpt.exception;

public class PaymentCallApiException extends RuntimeException {

    public PaymentCallApiException(String message) {
        super(message);
    }

    public PaymentCallApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
