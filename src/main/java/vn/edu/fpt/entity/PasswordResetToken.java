package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 255, nullable = false)
    private String token;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredDate;

    @Builder.Default
    @Column(name = "is_used")
    private Boolean isUsed = false;

}
