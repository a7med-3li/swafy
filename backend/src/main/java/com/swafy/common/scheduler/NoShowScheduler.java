package com.swafy.common.scheduler;

import com.swafy.ride.service.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class NoShowScheduler {

    private final RideService rideService;

    @Scheduled(cron = "0 */15 * * * *")
    public void processNoShows() {
        log.info("Checking for no-show rides...");
        rideService.markNoShows();
    }
}
