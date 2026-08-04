package vn.edu.fpt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.dto.revenue_manager.MonthlyRevenueForManagerDTO;
import vn.edu.fpt.dto.transaction_manager.CourseDTO;
import vn.edu.fpt.dto.transaction_manager.TransactionDetailDTO;
import vn.edu.fpt.dto.transaction_manager.TransactionListDTO;
import vn.edu.fpt.entity.Payment;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.dto.MonthlyRevenueDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = :status AND p.paidAt >= :startDate")
    BigDecimal sumAmountByStatusAndPaidAtAfter(@Param("status") PaymentStatus status, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT new vn.edu.fpt.dto.MonthlyRevenueDTO(YEAR(p.paidAt), MONTH(p.paidAt), COALESCE(SUM(p.amount), 0)) " +
            "FROM Payment p " +
            "WHERE p.status = :status AND p.paidAt >= :startDate " +
            "GROUP BY YEAR(p.paidAt), MONTH(p.paidAt) " +
            "ORDER BY YEAR(p.paidAt) ASC, MONTH(p.paidAt) ASC")
    List<MonthlyRevenueDTO> getMonthlyRevenue(@Param("status") PaymentStatus status, @Param("startDate") LocalDateTime startDate);

    Optional<Payment> findByGatewayOrderCode(String gatewayOrderCode);

    @Query("""
                  SELECT new vn.edu.fpt.dto.revenue_manager.MonthlyRevenueForManagerDTO(COALESCE(SUM(p.amount), 0))  FROM Payment p 
                  WHERE p.status = vn.edu.fpt.enums.PaymentStatus.PAID
                  AND CAST(p.paidAt as date) >= :startDate AND CAST(p.paidAt as date) <= :endDate 
            """)
    MonthlyRevenueForManagerDTO getMonthlyRevenueTotal(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("""
                  SELECT COUNT(p) FROM Payment p
            """)
    Integer getNumberAllPayment();

    @Query("""
                  SELECT COUNT(p) FROM Payment p WHERE p.status = :paymentStatus
            """)
    Integer getNumberAllPaymentWithStatus(@Param("paymentStatus") PaymentStatus paymentStatus);

    @Query("""
        SELECT p.status, count(p) FROM Payment p GROUP BY p.status
""")
    List<Object[]> gettransactionCountByStatusDTO();

    @Query("""
        SELECT new vn.edu.fpt.dto.transaction_manager.TransactionListDTO(p.id ,p.gatewayOrderCode, concat(p.order.user.lastName,' ', p.order.user.firstName), p.order.user.email, p.amount, p.description, p.status) FROM Payment p
        where (:status is null or p.status = :status)
        and (:fromDate is null or p.createdAt >= :fromDate)
        and (:toDate is null or p.createdAt < :toDate)
        and (:keyword is null or lower(coalesce(p.description, '') ) like lower(concat('%', :keyword, '%')) or lower(p.gatewayOrderCode) like lower(concat('%', :keyword, '%')) or lower(p.order.user.firstName) like 
        lower(concat('%', :keyword, '%')) or lower(p.order.user.lastName) like 
        lower(concat('%', :keyword, '%')) or lower(p.order.user.email) like lower(concat('%', :keyword, '%')))
        order by p.createdAt desc 
""")
    Page<TransactionListDTO> getTransactionByFilter(PaymentStatus status, LocalDateTime fromDate, LocalDateTime toDate, String keyword, Pageable pageable);

    @Query("""
        SELECT new vn.edu.fpt.dto.transaction_manager.TransactionDetailDTO(p.id, (concat(p.order.user.firstName, ' ', p.order.user.lastName)), p.order.user.email, p.amount, p.description, p.gatewayOrderCode, p.status, p.createdAt, p.updatedAt, p.expiredAt, p.paidAt, p.gateway, p.paymentUrl, null) FROM Payment p WHERE p.id = :paymentId
""")
    TransactionDetailDTO getTransactionDetailByPaymentId(@Param("paymentId") Integer paymentId);

    @Query("""
           SELECT new vn.edu.fpt.dto.transaction_manager.CourseDTO(oi.course.id, oi.course.title, oi.course.price, oi.course.thumbnailUrl) FROM Payment p JOIN p.order.items oi WHERE p.id = :paymentId
""")
    List<CourseDTO> getListItemByPaymentId(@Param("paymentId") Integer paymentId);

    @Query("""
        SELECT p FROM Payment p 
        WHERE p.status = vn.edu.fpt.enums.PaymentStatus.PENDING 
          AND p.expiredAt <= CURRENT_TIMESTAMP 
          AND p.updatedAt < (CURRENT_TIMESTAMP - 5 MINUTE)
        ORDER BY p.expiredAt ASC
    """)
    List<Payment> findExpiredPendingPayments();

    @Query("""
        SELECT p FROM Payment p 
        WHERE p.status = vn.edu.fpt.enums.PaymentStatus.PENDING
          AND p.createdAt > (CURRENT_TIMESTAMP - 30 MINUTE)
          AND (p.lastSyncedAt IS NULL OR p.lastSyncedAt < (CURRENT_TIMESTAMP - 5 MINUTE))
        ORDER BY p.createdAt ASC
    """)
    List<Payment> findPendingPaymentsForSync();
}
