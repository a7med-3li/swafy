package com.swafy;

import com.swafy.config.TwilioProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TwilioProperty.class)
public class SwafyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwafyApplication.class, args);
    }
}
