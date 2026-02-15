package com.swafy.config;

import com.swafy.common.util.HereApiProperties;
import lombok.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(HereApiProperties.class)
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder,
                               HereApiProperties props) {
        return builder
                .baseUrl(props.getBaseUrl())
                .build();
    }
}
