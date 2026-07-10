package org.example.backend.controller;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.entities.UserData;
import org.example.backend.service.OmdbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Plain unit tests for FavouritesController: verifies it reads the username from
 * the authenticated principal and delegates to the service.
 */
class FavouritesControllerTest {

    private final OmdbService omdbService = mock(OmdbService.class);
    private final FavouritesController controller = new FavouritesController(omdbService);
    private final OAuth2User principal = mock(OAuth2User.class);

    @BeforeEach
    void setUp() {
        when(principal.getAttribute("login")).thenReturn("alice");
    }

    @Test
    void getFavourites_delegatesToServiceWithPrincipalLogin() {
        when(omdbService.getFavorites("alice")).thenReturn(List.of(movie("tt1")));

        assertThat(controller.getFavourites(principal)).hasSize(1);
        verify(omdbService).getFavorites("alice");
    }

    @Test
    void addFavourite_delegatesAndReturnsUpdatedList() {
        UserData updated = UserData.builder()
                .userName("alice")
                .omdbMovieResponseList(List.of(movie("tt1")))
                .build();
        when(omdbService.addMovieToFavorites(eq("alice"), any(OmdbMovieResponse.class))).thenReturn(updated);

        assertThat(controller.addFavourite(movie("tt1"), principal)).hasSize(1);
        verify(omdbService).addMovieToFavorites(eq("alice"), any(OmdbMovieResponse.class));
    }

    @Test
    void removeFavourite_delegatesAndReturnsUpdatedList() {
        UserData updated = UserData.builder()
                .userName("alice")
                .omdbMovieResponseList(List.of())
                .build();
        when(omdbService.removeMovieFromFavorites("alice", "tt1")).thenReturn(updated);

        assertThat(controller.removeFavourite("tt1", principal)).isEmpty();
        verify(omdbService).removeMovieFromFavorites("alice", "tt1");
    }

    private OmdbMovieResponse movie(String imdbId) {
        return new OmdbMovieResponse(
                "Title", null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null, imdbId, null, null,
                null, null);
    }
}
