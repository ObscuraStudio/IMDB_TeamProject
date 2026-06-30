package org.example.backend.service;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.exception.MovieNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OmdbService {

    private final RestClient omdbRestClient;

    public OmdbService(RestClient omdbRestClient) {
        this.omdbRestClient = omdbRestClient;
    }

    // get a movie by its IMDb id, e.g. tt1375666
    public OmdbMovieResponse getMovieById(String imdbId) {
        OmdbMovieResponse movie = omdbRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/").queryParam("i", imdbId).build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new MovieNotFoundException("OMDb request failed: " + response.getStatusCode());
                })
                .body(OmdbMovieResponse.class);

        return verifyFound(movie);
    }

    // get a movie by its title (short plot)
    public OmdbMovieResponse getMovieByName(String title) {
        OmdbMovieResponse movie = omdbRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/").queryParam("t", title).build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new MovieNotFoundException("OMDb request failed: " + response.getStatusCode());
                })
                .body(OmdbMovieResponse.class);

        return verifyFound(movie);
    }

    // get a movie by its title, asking OMDb for the full plot text
    public OmdbMovieResponse getMoviePlotByName(String title) {
        OmdbMovieResponse movie = omdbRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/")
                        .queryParam("t", title)
                        .queryParam("plot", "full")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new MovieNotFoundException("OMDb request failed: " + response.getStatusCode());
                })
                .body(OmdbMovieResponse.class);

        return verifyFound(movie);
    }

    // OMDb answers HTTP 200 even when nothing matches, flagging it with "Response":"False".
    // This turns that case into a proper not-found.
    private OmdbMovieResponse verifyFound(OmdbMovieResponse movie) {
        if (movie == null || !"True".equalsIgnoreCase(movie.response())) {
            String reason = (movie != null && movie.error() != null) ? movie.error() : "No response from OMDb";
            throw new MovieNotFoundException(reason);
        }
        return movie;
    }
}
