package com.example.vinyl_record_collection_tracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Value("${discogs.token}")
    private String discogsToken;

    @Value("{discogs.base-url}")
    private String baseUrl;

    @Bean
    public WebClient discogsWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Discogs token=" + discogsToken)
                .defaultHeader("User-Agent", "VinylTracker/1.0")
                .build();
    }
}
