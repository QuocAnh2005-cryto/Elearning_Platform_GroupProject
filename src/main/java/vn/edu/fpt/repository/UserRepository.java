package vn.edu.fpt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.dto.revenue_manager.InstructorRevenueForManagerDTO;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.UserStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("""
			    SELECT DISTINCT u
			    FROM User u
			    LEFT JOIN FETCH u.userRoles ur
			    LEFT JOIN FETCH ur.role
			    WHERE u.email = :email
			""")
	Optional<User> findByEmail(String email);

	User save(User u);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	User findUserByPhone(String phone);

	@Query("SELECT u FROM User u " +
			"JOIN u.userRoles ur " +
			"JOIN ur.role r " +
			"WHERE LOWER(r.name) = 'instructor' " +
			"AND (:status IS NULL OR u.status = :status) " +
			"AND (:keyword IS NULL OR :keyword = '' " +
			"     OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
			"     OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
			"     OR LOWER(CONCAT(u.lastName, ' ', u.firstName)) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
	Page<User> searchAndFilterInstructors(
			@Param("keyword") String keyword,
			@Param("status") UserStatus status,
			Pageable pageable);

	@Query("SELECT u FROM User u " +
			"JOIN u.userRoles ur " +
			"JOIN ur.role r " +
			"WHERE LOWER(r.name) = 'manager' " +
			"AND (:status IS NULL OR u.status = :status) " +
			"AND (:keyword IS NULL OR :keyword = '' " +
			"     OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
			"     OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
			"     OR LOWER(CONCAT(u.lastName, ' ', u.firstName)) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
	Page<User> searchAndFilterManagers(
			@Param("keyword") String keyword,
			@Param("status") UserStatus status,
			Pageable pageable);

	@Query("SELECT COUNT(u) FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE LOWER(r.name) = 'instructor'")
	long countInstructors();

	@Query("SELECT COUNT(u) FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE LOWER(r.name) = 'learner'")
	long countLearners();

	boolean existsByEmailAndStatus(
			String email,
			UserStatus status);

	boolean existsByPhoneAndStatus(
			String phone,
			UserStatus status);

	@Query("""
            			select distinct u from User u join u.userRoles r where r.role.name = vn.edu.fpt.enums.RoleType.LEARNER
            			and (:keyword is null or lower(u.email) like lower(concat('%', :keyword, '%')) or lower(u.phone) like lower(concat('%', :keyword, '%')))
            """)
    Page<User> findAllLearnerByFilter(String keyword, Pageable pageable);

	@Query("""
		SELECT new vn.edu.fpt.dto.revenue_manager.InstructorRevenueForManagerDTO(
			u.id, u.firstName, u.lastName, u.email,
			COUNT(DISTINCT c.id),
			COALESCE(SUM(CASE WHEN (o.status = vn.edu.fpt.enums.OrderStatus.PAID OR o.status = vn.edu.fpt.enums.OrderStatus.COMPLETED) 
			                       AND (:month IS NULL OR MONTH(o.createdAt) = :month) 
			                       AND (:year IS NULL OR YEAR(o.createdAt) = :year) THEN 1 ELSE 0 END), 0),
			COALESCE(SUM(CASE WHEN (o.status = vn.edu.fpt.enums.OrderStatus.PAID OR o.status = vn.edu.fpt.enums.OrderStatus.COMPLETED) 
			                       AND (:month IS NULL OR MONTH(o.createdAt) = :month) 
			                       AND (:year IS NULL OR YEAR(o.createdAt) = :year) THEN oi.priceSnapshot ELSE 0 END), 0)
		)
		FROM User u
		JOIN u.userRoles ur
		JOIN ur.role r
		LEFT JOIN Course c ON c.instructor.id = u.id AND c.status = vn.edu.fpt.enums.CourseStatus.PUBLISHED
		LEFT JOIN OrderItem oi ON oi.course.id = c.id
		LEFT JOIN oi.order o
		WHERE LOWER(r.name) = 'instructor'
		AND (:keyword IS NULL OR :keyword = ''
			OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
			OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%'))
			OR LOWER(CONCAT(u.lastName, ' ', u.firstName)) LIKE LOWER(CONCAT('%', :keyword, '%')))
		GROUP BY u.id, u.firstName, u.lastName, u.email
	""")
	Page<InstructorRevenueForManagerDTO> getInstructorsRevenueStats(
			@Param("keyword") String keyword,
			@Param("month") Integer month,
			@Param("year") Integer year,
			Pageable pageable);
}
