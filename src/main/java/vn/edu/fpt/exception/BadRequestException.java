package vn.edu.fpt.exception;

/**
 * Thrown for invalid requests or business rule violations that should return HTTP 400.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
