package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.cart.OrderDto;
import vn.edu.fpt.dto.user.StudentPurchaseHistoryDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.mapper.DtoMapper;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryOrderService {

    private final OrderService orderService;
    private final DtoMapper dtoMapper;

    public StudentPurchaseHistoryDto getPurchaseHistoryData(User user, String status) {
        List<OrderDto> orderDtos = orderService.getPurchaseHistory(user, status);
        return StudentPurchaseHistoryDto.builder()
                .currentUser(dtoMapper.toUserDto(user))
                .orders(orderDtos)
                .build();
    }
}
