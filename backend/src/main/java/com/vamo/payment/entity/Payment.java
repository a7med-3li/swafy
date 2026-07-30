package com.vamo.payment.entity;

import com.vamo.common.enums.PaymentMethod;
import com.vamo.subscription.entity.Subscription;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;
    
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @NotNull
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    
    @Column
    private String paymentReference;
    
    @CreationTimestamp
    private Instant paidAt;

}
