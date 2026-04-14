package com.nikolaihoretski.tests.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Set<UserRole> createUserRoles = new HashSet<>();

    @OneToMany(mappedBy = "updatedBy", fetch = FetchType.LAZY)
    private Set<UserRole> updateUserRole = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserPermission> userPermissions = new HashSet<>();


    public void addRole(Role role) {
        boolean alreadyHasRole = this.userRoles.stream()
                .anyMatch(ur -> ur.getRole().equals(role));
        if (!alreadyHasRole) {
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(this);
            this.userRoles.add(userRole);
        }
    }

    public void addPermission(Permission permission) {
        boolean alreadyHasPermission = this.userPermissions.stream()
                .anyMatch(up -> up.getPermission().equals(permission));
        if (!alreadyHasPermission) {
            UserPermission userPermission = new UserPermission();
            userPermission.setPermission(permission);
            userPermission.setUser(this);
            this.userPermissions.add(userPermission);
        }
    }

}
