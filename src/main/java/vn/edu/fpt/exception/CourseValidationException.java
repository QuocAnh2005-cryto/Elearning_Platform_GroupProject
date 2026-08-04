package vn.edu.fpt.exception;

public class CourseValidationException extends RuntimeException {
    private String field;

    public CourseValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
