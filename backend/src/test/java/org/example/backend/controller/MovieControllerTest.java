package org.example.backend.controller;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.dto.Rating;
import org.example.backend.exception.MovieNotFoundException;
import org.example.backend.service.OmdbService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.servlet.OAuth2ClientWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web-layer tests for MovieController.
 * Service is mocked; this verifies routing, JSON serialization (note the OMDb
 * PascalCase keys via @JsonProperty), and that a MovieNotFoundException is
 * mapped to HTTP 404 by the @RestControllerAdvice.
 */
@WebMvcTest(controllers = MovieController.class,
        excludeAutoConfiguration = {
                OAuth2ClientAutoConfiguration.class,
                OAuth2ClientWebSecurityAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false) // skip the security filter chain; this slice tests routing, not auth
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OmdbService omdbService;

    // ---------- GET /api/movies/{imdbId} ----------

    @Test
    void getMovieById_whenFound_returnsMovie() throws Exception {
        when(omdbService.getMovieById("tt1375666")).thenReturn(sampleMovie());

        mockMvc.perform(get("/api/login/movies/tt1375666"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Title").value("Inception"))
                .andExpect(jsonPath("$.imdbID").value("tt1375666"))
                .andExpect(jsonPath("$.Type").value("movie"))
                .andExpect(jsonPath("$.Ratings[0].Source").value("Internet Movie Database"));
    }

    @Test
    void getMovieById_whenMissing_returns404WithErrorBody() throws Exception {
        when(omdbService.getMovieById("tt0000000"))
                .thenThrow(new MovieNotFoundException("Movie not found!"));

        mockMvc.perform(get("/api/login/movies/tt0000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("Movie not found")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // ---------- GET /api/movies/title/{title} ----------

    @Test
    void getMovieByName_whenFound_returnsMovie() throws Exception {
        when(omdbService.getMovieByName("Inception")).thenReturn(sampleMovie());

        mockMvc.perform(get("/api/login/movies/title/Inception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Title").value("Inception"))
                .andExpect(jsonPath("$.Year").value("2010"));
    }

    // ---------- GET /api/movies/plot/{title} ----------

    @Test
    void getMoviePlotByName_whenFound_returnsMovieWithPlot() throws Exception {
        when(omdbService.getMoviePlotByName("Inception")).thenReturn(sampleMovie());

        mockMvc.perform(get("/api/login/movies/plot/Inception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Plot").value("A thief who steals corporate secrets."));
    }

    private OmdbMovieResponse sampleMovie() {
        return new OmdbMovieResponse(
                "Inception", "2010", "PG-13", "16 Jul 2010", "148 min",
                "Action, Adventure, Sci-Fi", "Christopher Nolan", "Christopher Nolan",
                "Leonardo DiCaprio", "A thief who steals corporate secrets.",
                "English", "United States", "Won 4 Oscars",
                "https://img.example.com/inception.jpg",
                List.of(new Rating("Internet Movie Database", "8.8/10")),
                "74", "8.8", "2,000,000", "tt1375666", "movie", "$292,000,000",
                "True", null);
    }
}
