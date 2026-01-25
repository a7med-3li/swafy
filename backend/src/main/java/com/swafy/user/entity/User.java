package com.swafy.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swafy.common.enums.Gender;
import com.swafy.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

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
