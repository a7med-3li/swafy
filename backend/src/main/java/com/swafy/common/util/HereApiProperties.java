package com.swafy.common.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "here.api")
public class HereApiProperties {

    private String baseUrl;
    private String key;

    // getters and setters
}

