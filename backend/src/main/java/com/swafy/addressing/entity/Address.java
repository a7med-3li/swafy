package com.swafy.addressing.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name ="Address")
public class Address {
    // TODO: add the entity attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String Longitude;
    private String Latitude;
}
