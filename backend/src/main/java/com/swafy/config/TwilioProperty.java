package com.swafy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Primary
@ConfigurationProperties(prefix = "twilio")
public class TwilioProperty {

    private String accountSid;
    private String authToken;
    private String verifyServiceSid;
}