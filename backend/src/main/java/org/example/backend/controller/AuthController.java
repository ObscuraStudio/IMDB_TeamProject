package org.example.backend.controller;

import org.example.backend.service.OmdbService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OmdbService omdbService;

    public AuthController(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    @GetMapping("/me")
    public String getMe(@AuthenticationPrincipal OAuth2User user) {
        if (user != null) {
            String login = user.getAttribute("login").toString();
            // Load the user from Mongo, or create-and-store them on first login.
            omdbService.showUserData(login);
            return login;
        }
        return "";
    }
}
