package com.swafy.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swafy.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    private LocalDateTime createdAt;

    // Soft delete fields
    private boolean deleted = false;
    private LocalDateTime deletedAt;
}
