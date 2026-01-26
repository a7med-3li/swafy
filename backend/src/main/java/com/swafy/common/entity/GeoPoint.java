package com.swafy.common.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Embeddable
public class GeoPoint {

    private Long latitude;
    private Long longitude;
}
