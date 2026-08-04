package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.Cart;
import vn.edu.fpt.entity.User;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(User user);

    @Query("""
            select distinct c from Cart c 
            left join fetch c.items i 
            left join fetch i.course co 
            left join fetch co.instructor
            where c.user = :user
            """)
    Optional<Cart> findByUserWithItemsAndCourses(@Param("user") User user);
}
