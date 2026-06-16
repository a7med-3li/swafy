package com.swafy.passenger.entity;

import com.swafy.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "passenger_profiles")
public class PassengerProfile {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    private UUID homeStopId;     // preferred VBS for quick booking
    private int rideBalance;     // rides remaining from subscription
    private LocalDate subExpires;
    private int noShowCount;     // triggers warnings at 3, suspension at 5
}
