package vn.edu.fpt.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import vn.edu.fpt.entity.Role;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.RoleType;
import vn.edu.fpt.enums.UserStatus;
import vn.edu.fpt.repository.RoleRepository;
import vn.edu.fpt.repository.UserRepository;

@Service
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(
            UserRepository userRepository,
            RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(
            OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User =
                super.loadUser(request);

        String email =
                oAuth2User.getAttribute("email");

        String googleId =
                oAuth2User.getAttribute("sub");

        User user =
                userRepository
                        .findByEmail(email)
                        .orElse(null);

        if (user == null) {

            Role role =
                    roleRepository
                            .findByName(RoleType.LEARNER)
                            .orElseThrow();

            String firstName =
                    oAuth2User.getAttribute("given_name");

            String lastName =
                    oAuth2User.getAttribute("family_name");

            if (firstName == null || firstName.isBlank()) {
                firstName = "";
            }

            if (lastName == null || lastName.isBlank()) {
                lastName = "";
            }

            String picture =
                    oAuth2User.getAttribute("picture");

            user = new User();

            user.setEmail(email);

            user.setGoogleId(googleId);

            user.setFirstName(firstName);

            user.setLastName(lastName);

            user.setAvatarUrl(picture);

            user.setStatus(UserStatus.ACTIVE);

            user.addUserRole(role);

            user.setPhone(null);

            userRepository.save(user);

        } else {
            if (user.getStatus() == UserStatus.BANNED) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("user_banned"),
                        "Tài khoản của bạn đã bị khóa (BANNED). Vui lòng liên hệ quản trị viên."
                );
            }

            if (user.getStatus() == UserStatus.INACTIVE) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("user_inactive"),
                        "Tài khoản chưa được kích hoạt (INACTIVE). Vui lòng xác thực email."
                );
            }

            boolean isChanged = false;
            if (user.getGoogleId() == null) {
                user.setGoogleId(googleId);
                isChanged = true;
            }

            String picture = oAuth2User.getAttribute("picture");
            if (picture != null && !picture.isBlank() && (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank())) {
                user.setAvatarUrl(picture);
                isChanged = true;
            }

            if (isChanged) {
                userRepository.save(user);
            }
        }

        return new vn.edu.fpt.security.CustomOAuth2User(oAuth2User, user);
    }
}
