package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(length = 50, nullable = false)
    private String gateway;

    @Column(name = "gateway_order_code", length = 255, nullable = false, unique = true)
    private String gatewayOrderCode;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_url", length = 1000)
    private String paymentUrl;

    @Column(name = "qr_code_url", length = 1000)
    private String qrCodeUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus status;

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "gateway_response", columnDefinition = "NVARCHAR(MAX)")
    private String gatewayResponse;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    @Column(name = "account_number", length = 100)
    private String accountNumber;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "account_holder", length = 255)
    private String accountHolder;


    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

}
