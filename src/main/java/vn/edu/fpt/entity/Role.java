package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.enums.RoleType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true, length = 50)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    @Column(length = 255)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();

    public Set<User> getUsers() {
        Set<User> users = new HashSet<>();
        if (userRoles == null) {
            return users;
        }
        for (UserRole userRole : userRoles) {
            users.add(userRole.getUser());
        }
        return users;
    }

}
