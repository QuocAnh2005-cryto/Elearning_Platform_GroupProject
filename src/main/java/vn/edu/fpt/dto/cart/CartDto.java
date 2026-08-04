package vn.edu.fpt.dto.cart;

import lombok.*;
import vn.edu.fpt.dto.user.UserDto;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private Integer id;
    private UserDto user;
    private Set<CartItemDto> items;
}
