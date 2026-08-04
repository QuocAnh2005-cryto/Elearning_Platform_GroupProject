package vn.edu.fpt.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.SystemLog;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.fpt.enums.LogAction;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Integer> {
    List<SystemLog> findByUserIdOrderByCreatedAtDesc(Integer userId);

    @Query("SELECT l FROM SystemLog l JOIN FETCH l.user WHERE l.targetType = 'COURSE' AND l.targetId = :courseId ORDER BY l.createdAt DESC")
    List<SystemLog> findCourseLogs(@Param("courseId") String courseId);

    long countByTargetTypeAndTargetIdAndAction(String targetType, String targetId, LogAction action);
}
