package com.swafy.passenger.entity;

import com.swafy.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder
@Table(name = "passenger_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PassengerProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    private UUID homeStopId;     // preferred VBS for quick booking
    private int rideBalance;     // rides remaining from subscription
    private LocalDate subExpires;
    private int noShowCount;     // triggers warnings at 3, suspension at 5
}
