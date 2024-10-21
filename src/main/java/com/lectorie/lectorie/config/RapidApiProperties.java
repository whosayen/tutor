package com.lectorie.lectorie.config;// RapidApiProperties.java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RapidApiProperties {

    @Value("${rapidapi.base-url}")
    private String baseUrl;

    @Value("${rapidapi.key}")
    private String key;

    @Value("${rapidapi.host}")
    private String host;

    // Getters
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getKey() {
        return key;
    }

    public String getHost() {
        return host;
    }
}