package org.example.backend.service;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.entities.UserData;
import org.example.backend.exception.MovieNotFoundException;
import org.example.backend.repository.UserDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Unit tests for OmdbService.
 * <p>
 * OMDb is faked with MockRestServiceServer (bound to the RestClient builder),
 * so no real network calls and no API key are needed. The tests cover both the
 * happy path and OMDb's two failure modes: a real HTTP error, and its quirky
 * "HTTP 200 but Response:False" not-found response.
 */
class OmdbServiceTest {

    private static final String BASE_URL = "http://www.omdbapi.com";

    private static final String INCEPTION_JSON = """
            {
              "Title": "Inception",
              "Year": "2010",
              "Rated": "PG-13",
              "Runtime": "148 min",
              "Genre": "Action, Adventure, Sci-Fi",
              "Director": "Christopher Nolan",
              "Plot": "A thief who steals corporate secrets.",
              "Poster": "https://img.example.com/inception.jpg",
              "Ratings": [
                { "Source": "Internet Movie Database", "Value": "8.8/10" }
              ],
              "imdbRating": "8.8",
              "imdbID": "tt1375666",
              "Type": "movie",
              "Response": "True"
            }
            """;

    private static final String NOT_FOUND_JSON = """
            { "Response": "False", "Error": "Movie not found!" }
            """;

    private OmdbService omdbService;
    private MockRestServiceServer mockServer;
    private UserDataRepository mockUserDataRepository = mock(UserDataRepository.class);

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        mockServer = MockRestServiceServer.bindTo(builder).build();
        RestClient omdbRestClient = builder.build();
        omdbService = new OmdbService(omdbRestClient, mockUserDataRepository);
    }

    // ---------- getMovieById ----------

    @Test
    void getMovieById_mapsJsonToResponse() {
        mockServer.expect(method(HttpMethod.GET))
                .andExpect(queryParam("i", "tt1375666"))
                .andRespond(withSuccess(INCEPTION_JSON, MediaType.APPLICATION_JSON));

        OmdbMovieResponse movie = omdbService.getMovieById("tt1375666");

        assertThat(movie.title()).isEqualTo("Inception");
        assertThat(movie.year()).isEqualTo("2010");
        assertThat(movie.imdbId()).isEqualTo("tt1375666");
        assertThat(movie.imdbRating()).isEqualTo("8.8");
        assertThat(movie.ratings()).hasSize(1);
        assertThat(movie.ratings().getFirst().source()).isEqualTo("Internet Movie Database");
        mockServer.verify();
    }

    @Test
    void getMovieById_whenResponseFalse_throwsMovieNotFound() {
        mockServer.expect(method(HttpMethod.GET))
                .andRespond(withSuccess(NOT_FOUND_JSON, MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> omdbService.getMovieById("tt0000000"))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining("Movie not found");
        mockServer.verify();
    }

    @Test
    void getMovieById_whenHttpError_throwsMovieNotFound() {
        mockServer.expect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> omdbService.getMovieById("tt1375666"))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining("OMDb request failed");
        mockServer.verify();
    }

    // ---------- getMovieByName ----------

    @Test
    void getMovieByName_sendsTitleParamAndMaps() {
        mockServer.expect(method(HttpMethod.GET))
                .andExpect(queryParam("t", "Inception"))
                .andRespond(withSuccess(INCEPTION_JSON, MediaType.APPLICATION_JSON));

        OmdbMovieResponse movie = omdbService.getMovieByName("Inception");

        assertThat(movie.title()).isEqualTo("Inception");
        assertThat(movie.type()).isEqualTo("movie");
        mockServer.verify();
    }

    @Test
    void getMovieByName_whenResponseFalse_throwsMovieNotFound() {
        mockServer.expect(method(HttpMethod.GET))
                .andRespond(withSuccess(NOT_FOUND_JSON, MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> omdbService.getMovieByName("Nonexistent Movie"))
                .isInstanceOf(MovieNotFoundException.class);
        mockServer.verify();
    }

    // ---------- getMoviePlotByName ----------

    @Test
    void getMoviePlotByName_requestsFullPlot() {
        mockServer.expect(method(HttpMethod.GET))
                .andExpect(queryParam("t", "Inception"))
                .andExpect(queryParam("plot", "full"))
                .andRespond(withSuccess(INCEPTION_JSON, MediaType.APPLICATION_JSON));

        OmdbMovieResponse movie = omdbService.getMoviePlotByName("Inception");

        assertThat(movie.plot()).isEqualTo("A thief who steals corporate secrets.");
        mockServer.verify();
    }

    // ---------- users & favourites (Mongo repository is mocked) ----------

    @Test
    void createUser_whenNew_savesEmptyUser() {
        when(mockUserDataRepository.findById("alice")).thenReturn(Optional.empty());
        when(mockUserDataRepository.existsByUserName("alice")).thenReturn(false);
        when(mockUserDataRepository.save(any(UserData.class))).thenAnswer(inv -> inv.getArgument(0));

        UserData result = omdbService.createUser("alice");

        assertThat(result).isNotNull();
        assertThat(result.userName()).isEqualTo("alice");
        assertThat(result.omdbMovieResponseList()).isEmpty();
    }

    @Test
    void createUser_whenAlreadyExists_returnsNull() {
        when(mockUserDataRepository.findById("bob"))
                .thenReturn(Optional.of(UserData.builder().userName("bob").omdbMovieResponseList(List.of()).build()));

        assertThat(omdbService.createUser("bob")).isNull();
    }

    @Test
    void showUserData_whenExists_returnsExisting() {
        UserData existing = UserData.builder().userName("carol").omdbMovieResponseList(List.of()).build();
        when(mockUserDataRepository.findById("carol")).thenReturn(Optional.of(existing));

        assertThat(omdbService.showUserData("carol")).isEqualTo(existing);
    }

    @Test
    void showUserData_whenMissing_createsUser() {
        when(mockUserDataRepository.findById("dave")).thenReturn(Optional.empty());
        when(mockUserDataRepository.existsByUserName("dave")).thenReturn(false);
        when(mockUserDataRepository.save(any(UserData.class))).thenAnswer(inv -> inv.getArgument(0));

        UserData result = omdbService.showUserData("dave");

        assertThat(result.userName()).isEqualTo("dave");
    }

    @Test
    void addMovieToFavorites_addsCleanedMovie() {
        UserData user = UserData.builder().userName("erin").omdbMovieResponseList(new ArrayList<>()).build();
        when(mockUserDataRepository.findById("erin")).thenReturn(Optional.of(user));
        when(mockUserDataRepository.save(any(UserData.class))).thenAnswer(inv -> inv.getArgument(0));

        UserData result = omdbService.addMovieToFavorites("erin", movie("tt1"));

        assertThat(result.omdbMovieResponseList()).hasSize(1);
        assertThat(result.omdbMovieResponseList().getFirst().imdbId()).isEqualTo("tt1");
        // status fields are stripped before persisting
        assertThat(result.omdbMovieResponseList().getFirst().response()).isNull();
    }

    @Test
    void addMovieToFavorites_whenDuplicate_doesNotAddAgain() {
        UserData user = UserData.builder()
                .userName("erin")
                .omdbMovieResponseList(new ArrayList<>(List.of(movie("tt1").withResponse(null).withError(null))))
                .build();
        when(mockUserDataRepository.findById("erin")).thenReturn(Optional.of(user));

        UserData result = omdbService.addMovieToFavorites("erin", movie("tt1"));

        assertThat(result.omdbMovieResponseList()).hasSize(1);
    }

    @Test
    void getFavorites_returnsStoredList() {
        UserData user = UserData.builder()
                .userName("frank")
                .omdbMovieResponseList(List.of(movie("tt1")))
                .build();
        when(mockUserDataRepository.findById("frank")).thenReturn(Optional.of(user));

        assertThat(omdbService.getFavorites("frank")).hasSize(1);
    }

    @Test
    void removeMovieFromFavorites_removesMatchingId() {
        UserData user = UserData.builder()
                .userName("gina")
                .omdbMovieResponseList(new ArrayList<>(List.of(movie("tt1"), movie("tt2"))))
                .build();
        when(mockUserDataRepository.findById("gina")).thenReturn(Optional.of(user));
        when(mockUserDataRepository.save(any(UserData.class))).thenAnswer(inv -> inv.getArgument(0));

        UserData result = omdbService.removeMovieFromFavorites("gina", "tt1");

        assertThat(result.omdbMovieResponseList()).hasSize(1);
        assertThat(result.omdbMovieResponseList().getFirst().imdbId()).isEqualTo("tt2");
    }

    // Minimal movie with just an id (and a "True" status to prove it gets stripped).
    private OmdbMovieResponse movie(String imdbId) {
        return new OmdbMovieResponse(
                "Title", null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null, imdbId, null, null,
                "True", null);
    }
}
