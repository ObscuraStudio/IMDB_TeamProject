package org.example.backend.configuration;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

/**
 * Appends the OMDb {@code apikey} query parameter to every outgoing request.
 *
 * <p>This keeps the API key in a single place: the service layer builds plain
 * URLs (e.g. {@code ?i=tt1375666}) and never needs to know the secret.
 */
public class OmdbApiKeyInterceptor implements ClientHttpRequestInterceptor {

    private final String apiKey;

    public OmdbApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        URI uriWithKey = UriComponentsBuilder.fromUri(request.getURI())
                .queryParam("apikey", apiKey)
                .build(true)
                .toUri();

        // Wrap the original request so only the URI changes (method, headers preserved).
        HttpRequest requestWithKey = new HttpRequestWrapper(request) {
            @Override
            public URI getURI() {
                return uriWithKey;
            }
        };

        return execution.execute(requestWithKey, body);
    }
}
