package vn.edu.fpt.exception;

public class CourseSectionValidation extends RuntimeException {
    private String field;
    public CourseSectionValidation(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
