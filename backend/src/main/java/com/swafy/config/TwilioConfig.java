package com.swafy.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    private final TwilioProperty props;

    public TwilioConfig(@Qualifier("twilioProperty") TwilioProperty props) {
        this.props = props;
    }

    @PostConstruct
    public void init() {
        if (props.getAccountSid() == null || props.getAuthToken() == null) {
            throw new IllegalStateException(
                    "Twilio credentials are missing. Check application.yml or env variables."
            );
        }

        Twilio.init(
                props.getAccountSid(),
                props.getAuthToken()
        );
    }
}
