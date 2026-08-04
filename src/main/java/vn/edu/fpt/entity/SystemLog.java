package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import vn.edu.fpt.enums.LogAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "system_logs")
public class SystemLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 255, nullable = false)
    private LogAction action;

    @Column(name = "target_type", length = 100)
    private String targetType;

    @Column(name = "target_id", length = 100)
    private String targetId;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String meta;
}
