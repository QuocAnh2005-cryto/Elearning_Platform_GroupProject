package vn.edu.fpt.dto.user;

import lombok.*;
import vn.edu.fpt.dto.cart.OrderDto;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPurchaseHistoryDto {
    private UserDto currentUser;
    private List<OrderDto> orders;
}
