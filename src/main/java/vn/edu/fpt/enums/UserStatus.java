package vn.edu.fpt.enums;

public enum UserStatus {
    ACTIVE("Hoạt động"),
    BANNED("Bị khóa"),
    INACTIVE("Chưa kích hoạt");

    private final String label;

    UserStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
