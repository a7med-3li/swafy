package com.swafy.subscription.repository;

import com.swafy.corridor.entity.Corridor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepo extends JpaRepository<Corridor, Long> {

}
