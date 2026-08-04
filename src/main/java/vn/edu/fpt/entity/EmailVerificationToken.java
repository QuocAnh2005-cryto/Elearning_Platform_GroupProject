package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmailVerificationToken extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            unique = true,
            nullable = false
    )
    private User user;

    @Column(nullable = false,length = 6)
    private String otpCode;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Builder.Default
    private boolean used = false;

    @Builder.Default
    private Integer resendCount = 0;

}
