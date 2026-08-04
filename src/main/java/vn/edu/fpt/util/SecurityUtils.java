package vn.edu.fpt.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.security.CustomOAuth2User;
import vn.edu.fpt.security.CustomUserDetails;
import vn.edu.fpt.entity.User;
@org.springframework.stereotype.Component
public class SecurityUtils {

    private static UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        SecurityUtils.userRepository = userRepository;
    }

    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                user = ((CustomUserDetails) principal).getUser();
            } else if (principal instanceof CustomOAuth2User) {
                user = ((CustomOAuth2User) principal).getUser();
            } else if (principal instanceof User) {
                user = (User) principal;
            }
        }

        if (user != null && userRepository != null) {
            return userRepository.findById(user.getId()).orElse(user);
        }

        return user;
    }

    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }

    public static Integer getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static String getCurrentUserEmail() {
        User user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }
}