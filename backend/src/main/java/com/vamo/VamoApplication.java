package com.vamo;

import com.vamo.config.TwilioProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(TwilioProperty.class)
@EnableAsync
public class VamoApplication {
    public static void main(String[] args) {
        SpringApplication.run(VamoApplication.class, args);
    }
}
