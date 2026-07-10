package org.example.backend.controller;

import org.example.backend.entities.UserData;
import org.example.backend.service.OmdbService;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for MovieController#showUser — a user may only read their own data.
 */
class MovieControllerShowUserTest {

    private final OmdbService omdbService = mock(OmdbService.class);
    private final MovieController controller = new MovieController(omdbService);

    @Test
    void showUser_whenRequestingOwnData_returnsIt() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("login")).thenReturn("alice");
        UserData alice = UserData.builder().userName("alice").omdbMovieResponseList(List.of()).build();
        when(omdbService.showUserData("alice")).thenReturn(alice);

        assertThat(controller.showUser("alice", principal)).isEqualTo(alice);
    }

    @Test
    void showUser_whenRequestingOtherUsersData_returnsNull() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("login")).thenReturn("alice");

        assertThat(controller.showUser("bob", principal)).isNull();
    }

    @Test
    void showUser_whenAnonymous_returnsNull() {
        assertThat(controller.showUser("alice", null)).isNull();
    }
}
