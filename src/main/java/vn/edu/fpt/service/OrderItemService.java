package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.OrderItem;
import vn.edu.fpt.repository.OrderItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderItemService {
    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.repository = orderItemRepository;
    }

    public List<OrderItem> findAll() { return repository.findAll(); }
    public Optional<OrderItem> findById(Integer id) { return repository.findById(id); }
    public OrderItem save(OrderItem entity) { return repository.save(entity); }
    public void deleteById(Integer id) { repository.deleteById(id); }
    public boolean existsById(Integer id) { return repository.existsById(id); }
}
