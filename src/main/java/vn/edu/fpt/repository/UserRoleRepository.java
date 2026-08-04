package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}
