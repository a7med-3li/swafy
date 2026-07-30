package com.vamo.common.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Location {
    
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    private Double latitude;
    private Double longitude;
    
    private String addressName;
    
    public Location() {
    
    }
}
