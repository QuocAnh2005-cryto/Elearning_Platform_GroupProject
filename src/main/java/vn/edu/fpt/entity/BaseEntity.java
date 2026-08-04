package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate // tự động lưu thời điểm entity được tạo lần đầu
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // tự động cập nhật thời gian khi entity bị sửa đổi
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
