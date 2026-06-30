package org.example.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Basic setup for the baseURL and the API KEY from the env variables.
 *
 */
@Configuration
public class OmdbClientConfig {

    @Bean
    public RestClient omdbRestClient(
            @Value("${omdb.api.base-url}") String baseUrl,
            @Value("${omdb.api.key}") String apiKey,
            RestClient.Builder builder) {

        return builder
                .baseUrl(baseUrl)
                .requestInterceptor(new OmdbApiKeyInterceptor(apiKey))
                .build();
    }
}
