package vn.edu.fpt.dto.cart;

import lombok.*;
import vn.edu.fpt.dto.user.UserDto;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartPageDetailsDto {
    private CartDto cart;
    private Map<UserDto, List<CartItemDto>> itemsByInstructor;
    private int cartSize;
    
    private long subtotal;
    private long total;
    private long selectedItemsCount;
    
    private Map<Integer, String> instructorCheckboxState;
    private String globalCheckboxState;
}
