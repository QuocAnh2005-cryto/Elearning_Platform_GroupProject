package vn.edu.fpt.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.dto.revenueInstructor.CoursePerformanceDto;
import vn.edu.fpt.dto.revenueInstructor.CourseRevenueDto;
import vn.edu.fpt.dto.revenueInstructor.RecentOrderDto;
import vn.edu.fpt.entity.Order;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    List<Order> findByUserAndStatusIn(User user, List<OrderStatus> statuses);

    @Query("""
    select o from Order o where o.user = :user and o.status = vn.edu.fpt.enums.OrderStatus.PENDING
""")
    List<Order> findByUserAndStatus_Pending(@Param("user") User user);
}

