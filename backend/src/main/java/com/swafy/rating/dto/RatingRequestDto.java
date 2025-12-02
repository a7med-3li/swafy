package com.swafy.rating.dto;

public class RatingRequestDto {
    private Long rideId;
    private int rating;

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
