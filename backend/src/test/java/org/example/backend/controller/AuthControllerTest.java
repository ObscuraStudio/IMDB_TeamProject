package org.example.backend.controller;

import org.example.backend.service.OmdbService;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private final OmdbService omdbService = mock(OmdbService.class);
    private final AuthController controller = new AuthController(omdbService);

    @Test
    void getMe_whenAuthenticated_returnsLoginAndEnsuresUserExists() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("login")).thenReturn("alice");

        String result = controller.getMe(principal);

        assertThat(result).isEqualTo("alice");
        verify(omdbService).showUserData("alice"); // load-or-create on login
    }

    @Test
    void getMe_whenAnonymous_returnsEmptyAndTouchesNothing() {
        assertThat(controller.getMe(null)).isEmpty();
        verifyNoInteractions(omdbService);
    }
}
