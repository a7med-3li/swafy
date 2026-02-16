package com.swafy.addressing.entity;

import com.swafy.common.entity.GeoPoint;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@Table(name ="Address")
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    // TODO: add the entity attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address", unique = true)
    private String address;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
//            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
//    })
//    private GeoPoint location;

    private String latitude;
    private String longitude;
}
