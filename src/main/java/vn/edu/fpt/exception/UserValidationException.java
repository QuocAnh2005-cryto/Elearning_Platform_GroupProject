package vn.edu.fpt.exception;

public class UserValidationException extends RuntimeException {
    private final String field;

    public UserValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
