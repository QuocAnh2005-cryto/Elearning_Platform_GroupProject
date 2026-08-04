package vn.edu.fpt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.RegisterRequest;
import vn.edu.fpt.entity.EmailVerificationToken;
import vn.edu.fpt.entity.Role;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.RoleType;
import vn.edu.fpt.enums.UserStatus;
import vn.edu.fpt.repository.EmailVerificationTokenRepository;
import vn.edu.fpt.repository.RoleRepository;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.security.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;



    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            VerificationService verificationService,
            EmailService emailService,
            EmailVerificationTokenRepository emailVerificationTokenRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    public User register(RegisterRequest request) {

        Optional<User> opt = userRepository.findByEmail(request.getEmail());

        if(opt.isPresent()){

            User user = opt.get();

            if(user.getStatus() == UserStatus.ACTIVE){

                throw new RuntimeException("Email đã tồn tại");

            }

            // chưa active thì cập nhật lại thông tin

            user.setPasswordHash(
                    passwordEncoder.encode(
                            request.getPassword()));

            user.setFirstName(
                    request.getFirstName());

            user.setLastName(
                    request.getLastName());

            String phone = (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank())
                    ? request.getPhoneNumber().trim() : null;

            user.setPhone(phone);

            userRepository.save(user);

            EmailVerificationToken token =
                    verificationService.createOtp(user);

            emailService.sendVerifyEmail(

                    user.getEmail(),

                    token.getOtpCode()

            );

            return user;

        }


            Role role = roleRepository
                .findByName(RoleType.LEARNER)
                .orElseThrow();

        User user = new User();

        user.setEmail(request.getEmail());

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setFirstName(
                request.getFirstName());

        user.setLastName(
                request.getLastName());

        String phone = (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank())
                ? request.getPhoneNumber().trim() : null;

        user.setPhone(phone);

        user.setAvatarUrl("https://cdn2.fptshop.com.vn/small/avatar_trang_1_cd729c335b.jpg");

        user.setStatus(UserStatus.INACTIVE);


        user.addUserRole(role);

        user = userRepository.save(user);

        EmailVerificationToken token = verificationService.createOtp(user);

        emailService.sendVerifyEmail(user.getEmail(), token.getOtpCode());

        return user;
    }

    public void verifyOtp(Integer userId, String otp){
        User user = userRepository
                        .findById(userId)
                        .orElseThrow();

        EmailVerificationToken token = emailVerificationTokenRepository
                        .findByUser(user)
                        .orElseThrow();

        if(token.isUsed()){
            throw new RuntimeException("OTP đã được sử dụng !");
        }

        if(token.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP đã hết hạn !");
        }

        if(!token.getOtpCode().equals(otp)){

            throw new RuntimeException("OTP không chính xác !");

        }

        token.setUsed(true);
        user.setStatus(UserStatus.ACTIVE);
    }

    public void resendOtp(Integer userId){

        User user = userRepository.findById(userId).orElseThrow();
        EmailVerificationToken token = verificationService.createOtp(user);

        token.setResendCount(token.getResendCount()+1);

        emailService.sendVerifyEmail(

                user.getEmail(),

                token.getOtpCode()

        );

    }

    public boolean isActiveEmail(String email) {

        return userRepository.existsByEmailAndStatus(
                email,
                UserStatus.ACTIVE
        );

    }

    public boolean isActivePhone(String phone) {
        if (phone == null || phone.isBlank()) return false;
        return userRepository.existsByPhoneAndStatus(
                phone,
                UserStatus.ACTIVE
        );

    }
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone){
        if (phone == null || phone.isBlank()) return false;
        return userRepository.existsByPhone(phone);
    }

    public User createInstructorAccount(String firstName, String lastName, String email, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng trong hệ thống.");
        }
        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("Số điện thoại đã được sử dụng trong hệ thống.");
        }

        Role instructorRole = roleRepository.findByName(RoleType.INSTRUCTOR)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền INSTRUCTOR trong hệ thống"));

        String rawPassword = generateStrongPassword();

        User instructor = new User();
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);
        instructor.setEmail(email);
        instructor.setPhone(phone);
        instructor.setPasswordHash(passwordEncoder.encode(rawPassword));
        instructor.setAvatarUrl("https://cdn2.fptshop.com.vn/small/avatar_trang_1_cd729c335b.jpg");
        instructor.setStatus(UserStatus.ACTIVE);
        instructor.addUserRole(instructorRole);

        User savedUser = userRepository.save(instructor);

        emailService.sendInstructorCreatedEmail(savedUser.getEmail(), rawPassword, savedUser.getFirstName() + " " + savedUser.getLastName());

        return savedUser;
    }

    private String generateStrongPassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*";
        String allChars = upperCase + lowerCase + digits + specialChars;
        java.security.SecureRandom random = new java.security.SecureRandom();
        
        StringBuilder password = new StringBuilder();
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        java.util.List<Character> chars = new java.util.ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            chars.add(c);
        }
        java.util.Collections.shuffle(chars, random);
        StringBuilder finalPassword = new StringBuilder();
        for (char c : chars) {
            finalPassword.append(c);
        }
        
        return finalPassword.toString();
    }

}
