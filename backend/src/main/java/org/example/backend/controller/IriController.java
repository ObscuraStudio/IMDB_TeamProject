package org.example.backend.controller;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.entities.UserData;
import org.example.backend.service.OmdbService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iri/")
class IriController {

    private final OmdbService omdbService;

    public IriController(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    @GetMapping("/user/{username}")
    public UserData showUser(@PathVariable String username) {
        return omdbService.showUserData(username);
    }

/*    @PostMapping("/addFav")
    public ResponseEntity<UserData> addFavorite(
            @RequestBody OmdbMovieResponse movieResponse,
            @AuthenticationPrincipal OAuth2User principal // Holt den eingeloggten GitHub-User
    ) {
        // GitHub Username aus den OAuth2-Attributen extrahieren
        String githubUsername = principal.getAttribute("login");

        UserData updatedUser = omdbService.addMovieToFavorites(githubUsername, movieResponse);
        return ResponseEntity.ok(updatedUser);
    }*/

    @PostMapping("/addFav/{username}")
    public UserData addFavorite(
            @RequestBody OmdbMovieResponse movieResponse,
            @PathVariable String username /*@AuthenticationPrincipal OAuth2User principal */ // Holt den eingeloggten GitHub-User
    ) {
        // GitHub Username aus den OAuth2-Attributen extrahieren
        String githubUsername = username; //principal.getAttribute("login");

        UserData updatedUser = omdbService.addMovieToFavorites(githubUsername, movieResponse);
        return updatedUser;
    }


}
