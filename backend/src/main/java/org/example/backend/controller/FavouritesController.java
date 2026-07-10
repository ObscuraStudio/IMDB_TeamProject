package org.example.backend.controller;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.service.OmdbService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Favourites for the currently logged-in GitHub user.
 *
 * <p>The user is always taken from the authenticated principal (never a path
 * variable), so a user can only ever read or change their own favourites.
 * All endpoints live under {@code /api/**}, which SecurityConfig requires
 * authentication for.
 */
@RestController
@RequestMapping("/api/favourites")
public class FavouritesController {

    private final OmdbService omdbService;

    public FavouritesController(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    @GetMapping
    public List<OmdbMovieResponse> getFavourites(@AuthenticationPrincipal OAuth2User user) {
        return omdbService.getFavorites(login(user));
    }

    @PostMapping
    public List<OmdbMovieResponse> addFavourite(@RequestBody OmdbMovieResponse movie,
                                                @AuthenticationPrincipal OAuth2User user) {
        return omdbService.addMovieToFavorites(login(user), movie).omdbMovieResponseList();
    }

    @DeleteMapping("/{imdbId}")
    public List<OmdbMovieResponse> removeFavourite(@PathVariable String imdbId,
                                                   @AuthenticationPrincipal OAuth2User user) {
        return omdbService.removeMovieFromFavorites(login(user), imdbId).omdbMovieResponseList();
    }

    private String login(OAuth2User user) {
        return user.getAttribute("login").toString();
    }
}
