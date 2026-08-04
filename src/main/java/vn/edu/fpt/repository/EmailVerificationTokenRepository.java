package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.EmailVerificationToken;
import vn.edu.fpt.entity.User;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken,Integer> {

    Optional<EmailVerificationToken> findByUser(User user);

}
