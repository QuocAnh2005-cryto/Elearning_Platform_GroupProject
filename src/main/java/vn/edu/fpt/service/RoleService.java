package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.Role;
import vn.edu.fpt.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository roleRepository) {
        this.repository = roleRepository;
    }

    public List<Role> findAll() { return repository.findAll(); }
    public Optional<Role> findById(Integer id) { return repository.findById(id); }
    public Role save(Role entity) { return repository.save(entity); }
    public void deleteById(Integer id) { repository.deleteById(id); }
    public boolean existsById(Integer id) { return repository.existsById(id); }
}
