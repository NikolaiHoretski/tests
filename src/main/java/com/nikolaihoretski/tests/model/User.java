package com.nikolaihoretski.tests.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    private static final Long serialVersionID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createBy;

    @Column(name = "updated_by")
    private String updateBy;

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
