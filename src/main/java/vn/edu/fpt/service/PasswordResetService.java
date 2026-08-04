package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.ResetPasswordRequestDto;
import vn.edu.fpt.entity.PasswordResetToken;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.PasswordResetTokenRepository;
import vn.edu.fpt.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void sendResetLink(String email) {

        User user = userRepository.findByEmail(email)
                .orElse(null);

        // Không tiết lộ email có tồn tại hay không
        if (user == null) {
            return;
        }

        // Xoá token cũ (chỉ cho 1 token hợp lệ)
        tokenRepository.deleteByUser(user);

        String token = java.util.UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiredDate(LocalDateTime.now().plusMinutes(15))
                .isUsed(false)
                .build();

        tokenRepository.save(resetToken);

        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    public PasswordResetToken validateToken(String token) {

        Optional<PasswordResetToken> optionalToken =
                tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return null;
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (resetToken.getIsUsed()) {
            return null;
        }

        if (resetToken.getExpiredDate().isBefore(LocalDateTime.now())) {
            return null;
        }

        return resetToken;
    }

    public void resetPassword(ResetPasswordRequestDto request) {

        // 1. check password confirm
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // 2. validate token
        PasswordResetToken resetToken = validateToken(request.getToken());

        if(resetToken == null){
            return;
        }
        // 3. get user
        User user = resetToken.getUser();

        // 4. encode password
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        // 5. IMPORTANT: mark token as used
        resetToken.setIsUsed(true);
        tokenRepository.save(resetToken);
    }
}



