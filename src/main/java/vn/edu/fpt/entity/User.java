package vn.edu.fpt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.enums.UserStatus;
import vn.edu.fpt.enums.RoleType;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();

    @Size(min = 0, max = 50)
    @Column(columnDefinition = "Nvarchar(255)", nullable = false)
    private String firstName;

    @Size(min = 0, max = 50)
    @Column(columnDefinition = "Nvarchar(255)", nullable = false)
    private String lastName;

    @Email
    @NotBlank
    @Column(length= 255, nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = true)
    private String phone;

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = true)
    private String bio;

    @Column(length = 255, nullable = true)
    private String passwordHash;

    @Column(length = 500, nullable = true)
    private String avatarUrl;

    private String googleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserStatus status;

    @Builder.Default
    @Column(name = "favorite_setup_completed", nullable = false)
    private boolean favoriteSetupCompleted = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserFavoriteCategory> favoriteCategories = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();


    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments = new HashSet<>();


    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        if (userRoles == null) {
            return roles;
        }
        for (UserRole userRole : userRoles) {
            roles.add(userRole.getRole());
        }
        return roles;
    }

    public void addUserRole(Role role) {
        if (userRoles == null) {
            userRoles = new HashSet<>();
        }
        boolean exists = false;
        for (UserRole userRole : userRoles) {
            Role currentRole = userRole.getRole();
            if (currentRole == role) {
                exists = true;
                break;
            }
            if (currentRole.getId() != null
                    && role.getId() != null
                    && currentRole.getId().equals(role.getId())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            UserRole userRole = UserRole.builder()
                    .user(this)
                    .role(role)
                    .build();
            userRoles.add(userRole);
        }
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.setInstructor(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.setInstructor(null);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setUser(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setUser(null);
    }

    public String getFullAvatarUrl() {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return null;
        }

        if (avatarUrl.startsWith("http://")
                || avatarUrl.startsWith("https://")) {
            return avatarUrl;
        }

        return AppConstants.AZURE_STORAGE_BASE_URL + "/" +
                AppConstants.AZURE_STORAGE_CONTAINER_USER_AVATARS + "/" +
                avatarUrl;
    }

    public String getCustomAvatarUrl() {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return null;
        }
        
        if (googleId != null && !googleId.isBlank() && 
            (avatarUrl.startsWith("http://") || avatarUrl.startsWith("https://"))) {
            return avatarUrl;
        }

        if (!avatarUrl.startsWith("http://") && !avatarUrl.startsWith("https://")) {
            return AppConstants.AZURE_STORAGE_BASE_URL + "/" +
                    AppConstants.AZURE_STORAGE_CONTAINER_USER_AVATARS + "/" +
                    avatarUrl;
        }

        return null;
    }

    public RoleType getRole() {
        if (userRoles == null || userRoles.isEmpty()) {
            return RoleType.LEARNER;
        }
        for (UserRole ur : userRoles) {
            if (ur.getRole() != null) {
                return ur.getRole().getName();
            }
        }
        return RoleType.LEARNER;
    }
}