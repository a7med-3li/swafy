package com.vamo.subscription.entity;

import com.vamo.common.enums.SubscriptionStatus;
import com.vamo.corridor.entity.Corridor;
import com.vamo.passenger.entity.PassengerProfile;
import com.vamo.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private PassengerProfile passenger;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corridor_id", nullable = false)
    private Corridor corridor;
    
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate =  LocalDate.now().plusMonths(1);

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

    @Column(nullable = false)
    private LocalDate createdAt;
    
    @PostPersist
    private void postPersist() {
        this.createdAt = LocalDate.now();
    }
}
