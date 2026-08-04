package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.Cart;
import vn.edu.fpt.entity.CartItem;
import vn.edu.fpt.entity.Course;
import vn.edu.fpt.repository.CartItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartItemService {
    private final CartItemRepository repository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.repository = cartItemRepository;
    }

    public boolean addCourseToCart(Cart cart, Course course) {
        if (repository.existsByCartAndCourse(cart, course)) {
            return false; // Already in cart
        }
        CartItem item = CartItem.builder()
                .cart(cart)
                .course(course)
                .build();
        repository.save(item);
        return true;
    }

    public int countItemsInCart(Cart cart) {
        Integer count = repository.countByCart(cart);
        return count != null ? count : 0;
    }

    public List<CartItem> findAll() { return repository.findAll(); }
    public Optional<CartItem> findById(Integer id) { return repository.findById(id); }
    public CartItem save(CartItem entity) { return repository.save(entity); }
    public void deleteById(Integer id) { repository.deleteById(id); }
    public boolean existsById(Integer id) { return repository.existsById(id); }
}
