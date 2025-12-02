package com.swafy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String example;

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }
}
