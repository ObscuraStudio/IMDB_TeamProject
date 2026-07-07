package org.example.backend.controller;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.service.OmdbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class MovieController {

    private final OmdbService omdbService;

    public MovieController(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    // GET a movie by its IMDb id, e.g. /api/movies/tt1375666
    @GetMapping("/movies/{imdbId}")
    public OmdbMovieResponse getMovieById(@PathVariable String imdbId) {
        return omdbService.getMovieById(imdbId);
    }

    // GET a movie by its title, e.g. /api/movies/title/Inception
    @GetMapping("/movies/title/{title}")
    public OmdbMovieResponse getMovieByName(@PathVariable String title) {
        return omdbService.getMovieByName(title);
    }

    // GET a movie by its title with the full plot, e.g. /api/movies/plot/Inception
    @GetMapping("/movies/plot/{title}")
    public OmdbMovieResponse getMoviePlotByName(@PathVariable String title) {
        return omdbService.getMoviePlotByName(title);
    }
}
