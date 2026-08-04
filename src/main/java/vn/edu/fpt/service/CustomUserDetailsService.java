package vn.edu.fpt.service;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.UserStatus;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.security.CustomUserDetails;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản hoặc mật khẩu không chính xác"));

        if (user.getStatus() == UserStatus.BANNED) {
            throw new LockedException("Tài khoản của bạn đã bị khóa (BANNED). Vui lòng liên hệ quản trị viên.");
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new DisabledException("Tài khoản chưa được kích hoạt (INACTIVE). Vui lòng xác thực email.");
        }

        return new CustomUserDetails(user);
    }
}
