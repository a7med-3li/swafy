package com.vamo.addressing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Builder
@Table(name ="Address")
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    //TODO: refactor this
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String title;
    
    @Column(name = "description", unique = true)
    private String description;

    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
}
