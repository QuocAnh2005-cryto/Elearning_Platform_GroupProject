package vn.edu.fpt.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import vn.edu.fpt.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final User user;

    public CustomOAuth2User(OAuth2User oauth2User, User user) {
        this.oauth2User = oauth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();
        if (user.getRoles() != null) {
            for (vn.edu.fpt.entity.Role role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(
                        "ROLE_" + role.getName().name()
                ));
            }
        }
        return authorities.isEmpty() ? oauth2User.getAuthorities() : authorities;
    }

    @Override
    public String getName() {
        return oauth2User.getName();
    }

    public User getUser() {
        return user;
    }
}
