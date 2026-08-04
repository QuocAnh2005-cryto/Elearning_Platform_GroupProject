package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.EmailVerificationToken;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.EmailVerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationService {

    private final EmailVerificationTokenRepository repository;

    public String generateOtp(){

        return String.valueOf(

                ThreadLocalRandom.current().nextInt(100000, 999999)

        );

    }

    public EmailVerificationToken createOtp(User user){

        String otp = generateOtp();

        EmailVerificationToken token = repository.findByUser(user).orElse(

                                EmailVerificationToken
                                        .builder()
                                        .user(user)
                                        .build()

                        );

        token.setOtpCode(otp);

        token.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        token.setUsed(false);

        return repository.save(token);

    }

}
