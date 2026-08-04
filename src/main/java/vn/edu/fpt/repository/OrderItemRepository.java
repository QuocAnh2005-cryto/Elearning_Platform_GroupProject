package vn.edu.fpt.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.dto.revenueInstructor.CoursePerformanceDto;
import vn.edu.fpt.dto.revenueInstructor.CourseRevenueDto;
import vn.edu.fpt.dto.revenueInstructor.RecentOrderDto;
import vn.edu.fpt.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("""
          select COALESCE(sum(oi.priceSnapshot), 0) from OrderItem oi
          where oi.course.instructor.id = :instructorId
          and oi.order.status in (vn.edu.fpt.enums.OrderStatus.PAID, vn.edu.fpt.enums.OrderStatus.COMPLETED)
          and oi.order.createdAt between :fromDate and :toDate
          """)
    BigDecimal sumTotalRevenueByInstructor(@Param("instructorId") Integer instructorId,
                                           @Param("fromDate") LocalDateTime fromDate,
                                           @Param("toDate") LocalDateTime toDate);

    @Query("""
          select count(distinct oi.order.id) from OrderItem oi
          where oi.course.instructor.id = :instructorId
          and oi.order.status in (vn.edu.fpt.enums.OrderStatus.PAID, vn.edu.fpt.enums.OrderStatus.COMPLETED)
          and oi.order.createdAt between :fromDate and :toDate
          """)
    Long countOrder(@Param("instructorId") Integer instructorId,
                    @Param("fromDate") LocalDateTime fromDate,
                    @Param("toDate") LocalDateTime toDate);

    @Query("""
         select new vn.edu.fpt.dto.revenueInstructor.CourseRevenueDto(oi.course.title, SUM(oi.priceSnapshot))
         from OrderItem oi
         where oi.course.instructor.id = :instructorId
         and oi.order.status in (vn.edu.fpt.enums.OrderStatus.PAID, vn.edu.fpt.enums.OrderStatus.COMPLETED)
         and oi.order.createdAt between :fromDate and :toDate
         group by oi.course.title order by sum(oi.priceSnapshot) desc
         """)
    List<CourseRevenueDto> topSellingCourse(@Param("instructorId") Integer instructorId,
                                            @Param("fromDate") LocalDateTime fromDate,
                                            @Param("toDate") LocalDateTime toDate,
                                            Pageable pagebale);

    @Query("""
          select new vn.edu.fpt.dto.revenueInstructor.CoursePerformanceDto(oi.course.title, count(oi))
          from OrderItem oi
          where oi.course.instructor.id = :instructorId
          and oi.order.status in (vn.edu.fpt.enums.OrderStatus.PAID, vn.edu.fpt.enums.OrderStatus.COMPLETED)
          and oi.order.createdAt between :fromDate and :toDate
          group by oi.course.title order by count(oi) desc
          """)
    List<CoursePerformanceDto> coursePerformance(@Param("instructorId") Integer instructorId,
                                                 @Param("fromDate") LocalDateTime fromDate,
                                                 @Param("toDate") LocalDateTime toDate,
                                                 Pageable pageable);

    // Instructor dashboard/orders: cot nao co trong Entity thi lay ra, khong them field gia.
    @Query("""
           select new vn.edu.fpt.dto.revenueInstructor.RecentOrderDto(
               oi.order.id,
               oi.order.user.firstName,
               oi.order.user.lastName,
               oi.order.user.email,
               oi.course.id,
               coalesce(oi.courseTitleSnapshot, oi.course.title),
               oi.course.thumbnailUrl,
               oi.course.category.name,
               oi.course.description,
               oi.course.level,
               oi.course.status,
               oi.order.createdAt,
               oi.course.price,
               oi.priceSnapshot,
               oi.order.status,
               p.status
           )
           from OrderItem oi
           left join oi.order.payment p
           where oi.course.instructor.id = :instructorId
           order by oi.order.createdAt desc
           """)
    List<RecentOrderDto> recentOrder(@Param("instructorId") Integer instructorId,
                                     Pageable pageable);

    // Instructor orders full list: dung cho nut "Xem tat ca" tren dashboard.
    @Query("""
           select new vn.edu.fpt.dto.revenueInstructor.RecentOrderDto(
               oi.order.id,
               oi.order.user.firstName,
               oi.order.user.lastName,
               oi.order.user.email,
               oi.course.id,
               coalesce(oi.courseTitleSnapshot, oi.course.title),
               oi.course.thumbnailUrl,
               oi.course.category.name,
               oi.course.description,
               oi.course.level,
               oi.course.status,
               oi.order.createdAt,
               oi.course.price,
               oi.priceSnapshot,
               oi.order.status,
               p.status
           )
           from OrderItem oi
           left join oi.order.payment p
           where oi.course.instructor.id = :instructorId
           order by oi.order.createdAt desc
           """)
    List<RecentOrderDto> findInstructorOrders(@Param("instructorId") Integer instructorId);

    // Instructor order detail: chi tiet 1 order, chi lay course thuoc instructor dang dang nhap.
    @Query("""
           select new vn.edu.fpt.dto.revenueInstructor.RecentOrderDto(
               oi.order.id,
               oi.order.user.firstName,
               oi.order.user.lastName,
               oi.order.user.email,
               oi.course.id,
               coalesce(oi.courseTitleSnapshot, oi.course.title),
               oi.course.thumbnailUrl,
               oi.course.category.name,
               oi.course.description,
               oi.course.level,
               oi.course.status,
               oi.order.createdAt,
               oi.course.price,
               oi.priceSnapshot,
               oi.order.status,
               p.status
           )
           from OrderItem oi
           left join oi.order.payment p
           where oi.course.instructor.id = :instructorId
           and oi.order.id = :orderId
           order by oi.id asc
           """)
    List<RecentOrderDto> findInstructorOrderDetails(@Param("instructorId") Integer instructorId,
                                                    @Param("orderId") Integer orderId);

    @Query(value = "select convert(DATE, o.created_at) as d, sum(oi.price_snapshot) as rev \n" +
            "          from order_items oi \n" +
            "          join orders o on oi.order_id = o.id\n" +
            "          join courses c on c.id = oi.course_id\n" +
            // Instructor dashboard chart: PAID/COMPLETED moi tinh doanh thu, CANCELLED/EXPIRED khong vao chart.
            "          where c.instructor_id = :instructorId and o.status in ('PAID', 'COMPLETED') and o.created_at between :fromDate and :toDate\n" +
            "          group by convert(DATE, o.created_at) order by d", nativeQuery = true)
    List<Object[]> revenueTrend(@Param("instructorId") Integer instructorId,
                                @Param("fromDate") LocalDateTime fromDate,
                                @Param("toDate") LocalDateTime toDate);
}
