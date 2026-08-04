package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.cart.OrderDto;
import vn.edu.fpt.entity.Order;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.OrderRepository;

import vn.edu.fpt.enums.OrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    private final OrderRepository repository;
    private final DtoMapper dtoMapper;

    public OrderService(OrderRepository orderRepository, DtoMapper dtoMapper) {
        this.repository = orderRepository;
        this.dtoMapper = dtoMapper;
    }

    public List<Order> findAll() { return repository.findAll(); }
    public Optional<Order> findById(Integer id) { return repository.findById(id); }
    public Order save(Order entity) { return repository.save(entity); }
    public void deleteById(Integer id) { repository.deleteById(id); }
    public boolean existsById(Integer id) { return repository.existsById(id); }

    public List<OrderDto> getPurchaseHistory(User user, String status) {
        if (user == null) {
            return Collections.emptyList();
        }
        
        List<Order> orders;
        if (status == null || status.equalsIgnoreCase("ALL")) {
            orders = repository.findByUser(user);
        } else {
            List<OrderStatus> statusList = new ArrayList<>();
            if (status.equalsIgnoreCase("SUCCESS")) {
                statusList.add(OrderStatus.PAID);
                statusList.add(OrderStatus.COMPLETED);
            } else if (status.equalsIgnoreCase("EXPIRED")) {
                statusList.add(OrderStatus.EXPIRED);
                statusList.add(OrderStatus.CANCELLED);
            } else if (status.equalsIgnoreCase("PENDING")) {
                statusList.add(OrderStatus.PENDING);
            }
            
            if (statusList.isEmpty()) {
                orders = repository.findByUser(user);
            } else {
                orders = repository.findByUserAndStatusIn(user, statusList);
            }
        }
        
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(dtoMapper.toOrderDto(order));
        }
        return orderDtos;
    }

    public List<Order> findOrderByUser(User user) {
        return repository.findByUserAndStatus_Pending(user);
    }
}
