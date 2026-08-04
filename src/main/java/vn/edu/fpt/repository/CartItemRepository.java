package vn.edu.fpt.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.Cart;
import vn.edu.fpt.entity.CartItem;
import vn.edu.fpt.entity.Course;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartAndCourse(Cart cart, Course course);
    boolean existsByCartAndCourse(Cart cart, Course course);
    Integer countByCart(Cart cart);
}
