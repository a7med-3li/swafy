package com.vamo.passenger.entity;

import com.vamo.subscription.entity.Subscription;
import com.vamo.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_profile_id")
    private List<Subscription> subscriptions;
    
    
    private UUID homeStopId;     // preferred VBS for quick booking
    
}
