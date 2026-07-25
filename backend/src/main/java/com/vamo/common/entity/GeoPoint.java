package com.vamo.common.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Embeddable
public class GeoPoint {

    private Double latitude;
    private Double longitude;
}
