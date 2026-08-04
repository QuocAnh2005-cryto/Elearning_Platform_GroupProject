package vn.edu.fpt.dto.revenueInstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.enums.OrderStatus;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.util.AppConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class RecentOrderDto {
    private Integer orderId;
    private String firstName;
    private String lastName;
    private String studentEmail;
    private Integer courseId;
    private String courseName;
    private String courseThumbnailUrl;
    private String courseCategoryName;
    private String courseDescription;
    private String courseLevel;
    private CourseStatus courseStatus;
    private LocalDateTime purchaseDate;
    private BigDecimal originalPrice;
    private BigDecimal finalPrice;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;

    public RecentOrderDto(Integer orderId,
                          String firstName,
                          String lastName,
                          String studentEmail,
                          Integer courseId,
                          String courseName,
                          String courseThumbnailUrl,
                          String courseCategoryName,
                          String courseDescription,
                          String courseLevel,
                          CourseStatus courseStatus,
                          LocalDateTime purchaseDate,
                          BigDecimal originalPrice,
                          BigDecimal finalPrice,
                          OrderStatus orderStatus,
                          PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentEmail = studentEmail;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseThumbnailUrl = courseThumbnailUrl;
        this.courseCategoryName = courseCategoryName;
        this.courseDescription = courseDescription;
        this.courseLevel = courseLevel;
        this.courseStatus = courseStatus;
        this.purchaseDate = purchaseDate;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
    }

    public String getStudentDisplayName() {
        String fullName = ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
        return fullName.isEmpty() ? studentEmail : fullName;
    }

    public BigDecimal getDiscount() {
        BigDecimal original = originalPrice == null ? BigDecimal.ZERO : originalPrice;
        BigDecimal paid = finalPrice == null ? BigDecimal.ZERO : finalPrice;
        BigDecimal discount = original.subtract(paid);
        return discount.compareTo(BigDecimal.ZERO) > 0 ? discount : BigDecimal.ZERO;
    }

    public BigDecimal getInstructorRevenue() {
        // Instructor order revenue: chi don hang da thanh toan thanh cong moi tinh tien cho giang vien.
        // Pending/Cancelled/Expired/Failed chi hien trong lich su don hang, doanh thu phai bang 0.
        boolean paidByPayment = paymentStatus == PaymentStatus.PAID;
        boolean paidByOrder = paymentStatus == null && (orderStatus == OrderStatus.PAID || orderStatus == OrderStatus.COMPLETED);
        if (!paidByPayment && !paidByOrder) {
            return BigDecimal.ZERO;
        }
        BigDecimal paid = finalPrice == null ? BigDecimal.ZERO : finalPrice;
        // Dashboard instructor/order list: tien giang vien nhan = tien hoc vien tra sau khi tru phi nen tang.
        return paid.multiply(BigDecimal.valueOf(1 - AppConstants.PLATFORM_FEE));
    }

    public String getPaymentStatusText() {
        if (paymentStatus != null) {
            return switch (paymentStatus) {
                case PAID -> "Paid";
                case PENDING -> "Pending";
                case FAILED -> "Failed";
                case CANCELLED -> "Cancelled";
                case EXPIRED -> "Expired";
            };
        }
        if (orderStatus == null) {
            return "Unknown";
        }
        return switch (orderStatus) {
            case PAID, COMPLETED -> "Paid";
            case PENDING -> "Pending";
            case CANCELLED -> "Cancelled";
            case EXPIRED -> "Expired";
        };
    }
}
