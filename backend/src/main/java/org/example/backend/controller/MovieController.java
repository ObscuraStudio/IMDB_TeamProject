package org.example.backend.controller;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.entities.UserData;
import org.example.backend.service.OmdbService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final OmdbService omdbService;

    public MovieController(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    // GET a movie by its IMDb id, e.g. /api/movies/tt1375666
    @GetMapping("/{imdbId}")
    public OmdbMovieResponse getMovieById(@PathVariable String imdbId) {
        return omdbService.getMovieById(imdbId);
    }

    // GET a movie by its title, e.g. /api/movies/title/Inception
    @GetMapping("/title/{title}")
    public OmdbMovieResponse getMovieByName(@PathVariable String title) {
        return omdbService.getMovieByName(title);
    }

    // GET a movie by its title with the full plot, e.g. /api/movies/plot/Inception
    @GetMapping("/plot/{title}")
    public OmdbMovieResponse getMoviePlotByName(@PathVariable String title) {
        return omdbService.getMoviePlotByName(title);
    }

    @GetMapping("/user/{username}")
    public UserData showUser(@PathVariable String username, @AuthenticationPrincipal OAuth2User user) {
        if(user!=null){
            return user.getAttribute("login").toString().compareTo(username)==0 ?
                    omdbService.showUserData(username)
                    :
                    null;
        }
        // unreachable code
        return null;
    }
}
