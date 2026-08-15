package com.vamo.corridor.repository;

import java.util.List;
import java.util.UUID;
import com.vamo.common.enums.SubscriptionStatus;
import com.vamo.corridor.entity.Corridor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CorridorRepository extends JpaRepository<Corridor, Long> {
	
	@Query("SELECT c FROM Corridor c WHERE NOT EXISTS (" +
			"SELECT 1 FROM Subscription s " +
			"WHERE s.corridor.id = c.id " +
			"AND s.passenger.id = :passengerId " +
			"AND s.status IN (:blockingStatuses)" +
			")")
	List<Corridor> findAvailableCorridors(
			@Param("passengerId") UUID passengerId,
			@Param("blockingStatuses") List<SubscriptionStatus> blockingStatuses
	);
}
