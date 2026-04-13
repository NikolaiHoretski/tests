package com.nikolaihoretski.tests.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity
@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Permission extends BaseEntity<User> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

 }
