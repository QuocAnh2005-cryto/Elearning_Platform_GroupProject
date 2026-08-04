package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByParentIsNullAndStatus(String status);

    List<Category> findByParentIsNotNullAndStatus(String status);

    List<Category> findByParentIdAndStatus(Integer parentId, String status);

    Optional<Category> findByIdAndStatus(Integer id, String status);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.status = :status")
    List<Category> findByStatus(@Param("status") String status);

    @Query("SELECT c.id, COUNT(co.id) FROM Category c " +
           "LEFT JOIN Course co ON co.category = c AND co.status = vn.edu.fpt.enums.CourseStatus.PUBLISHED " +
           "WHERE c.status = :status " +
           "GROUP BY c.id")
    List<Object[]> findCourseCountsByCategoryStatus(@Param("status") String status);
}
