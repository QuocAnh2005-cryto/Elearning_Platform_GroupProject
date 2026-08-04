package vn.edu.fpt.dto.cart;

import lombok.*;
import vn.edu.fpt.dto.course.OrderItemDto;
import vn.edu.fpt.dto.user.UserDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Integer id;
    private UserDto user;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private Set<OrderItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
