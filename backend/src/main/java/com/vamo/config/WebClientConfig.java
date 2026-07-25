package com.vamo.config;

import com.vamo.common.util.HereApiProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(HereApiProperties.class)
public class WebClientConfig {

    @Bean
    @Qualifier("routingClient")
    public WebClient routeClient(WebClient.Builder builder,
                                 HereApiProperties props) {
        return builder
                .baseUrl(props.getRouteUrl())
                .build();
    }

    @Bean
    @Qualifier("searchClient")
    public WebClient searchClient(WebClient.Builder builder,
                                 HereApiProperties props) {
        return builder
                .baseUrl(props.getSearchUrl())
                .build();
    }
}
