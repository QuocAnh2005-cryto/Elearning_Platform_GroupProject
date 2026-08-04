package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.PasswordResetToken;
import vn.edu.fpt.repository.PasswordResetTokenRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository repository;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.repository = passwordResetTokenRepository;
    }

    public List<PasswordResetToken> findAll() { return repository.findAll(); }
    public Optional<PasswordResetToken> findById(Integer id) { return repository.findById(id); }
    public PasswordResetToken save(PasswordResetToken entity) { return repository.save(entity); }
    public void deleteById(Integer id) { repository.deleteById(id); }
    public boolean existsById(Integer id) { return repository.existsById(id); }
}
