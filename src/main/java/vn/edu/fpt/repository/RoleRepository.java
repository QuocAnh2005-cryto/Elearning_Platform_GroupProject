package vn.edu.fpt.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.Role;

import vn.edu.fpt.enums.RoleType;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(RoleType name);
}

