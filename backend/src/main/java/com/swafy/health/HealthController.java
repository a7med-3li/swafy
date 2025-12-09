package com.swafy.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {
    @GetMapping("/health")
    public Map<String, String> health() {
        System.out.println("We are hereeeeeeee, 2ed of December");
        return Map.of("status", "ok");
    }


    @GetMapping("/start")
    public String start() {
        System.out.println("We are hereeeeeeee, 2ed of December");
        return "We are hereeeeeeee, 2ed of December";
    }
}
