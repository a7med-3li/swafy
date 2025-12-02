package com.swafy.rating.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RideRating {
    @Id
    @GeneratedValue
    private Long id;
    private Long rideId;
    private int rating;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
