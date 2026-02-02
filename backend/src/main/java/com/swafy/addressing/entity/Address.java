package com.swafy.addressing.entity;

import com.swafy.common.entity.GeoPoint;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
    private GeoPoint location;
}
