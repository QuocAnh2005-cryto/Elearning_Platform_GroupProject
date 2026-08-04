package vn.edu.fpt.dto.transaction_manager;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class TransactionDetailDTO {

    private Integer id;
    private String customerName;
    private String customerEmail;
    private BigDecimal amount;
    private String description;
    private String gateWayOrderCode;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime paidAt;
    private String gateWay;
    private String paymentUrl;
    List<CourseDTO> courses;

    public TransactionDetailDTO(Integer id, String customerName, String customerEmail, BigDecimal amount, String description, String gateWayOrderCode, PaymentStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime expiredAt, LocalDateTime paidAt, String gateWay, String paymentUrl, List<CourseDTO> courses) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.amount = amount;
        this.description = description;
        this.gateWayOrderCode = gateWayOrderCode;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expiredAt = expiredAt;
        this.paidAt = paidAt;
        this.gateWay = gateWay;
        this.paymentUrl = paymentUrl;
        this.courses = courses;
    }

    public String getStatusLabel() {
        return status != null ? status.getLabel() : "";
    }
}
