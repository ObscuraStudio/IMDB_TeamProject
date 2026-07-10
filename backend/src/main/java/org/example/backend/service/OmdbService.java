package org.example.backend.service;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.entities.UserData;
import org.example.backend.exception.MovieNotFoundException;
import org.example.backend.repository.UserDataRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class OmdbService {

    private final UserDataRepository userDataRepository;

    private final RestClient omdbRestClient;

    public OmdbService(RestClient omdbRestClient, UserDataRepository userDataRepository) {
        this.omdbRestClient = omdbRestClient;
        this.userDataRepository = userDataRepository;
    }

    // get a movie by its IMDb id, e.g. tt1375666
    public OmdbMovieResponse getMovieById(String imdbId) {
        OmdbMovieResponse movie = omdbRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/").queryParam("i", imdbId).build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new MovieNotFoundException("OMDb request failed: " + response.getStatusCode());
                })
                .body(OmdbMovieResponse.class);

        return verifyFound(movie);
    }

    // get a movie by its title (short plot)
    public OmdbMovieResponse getMovieByName(String title) {
        OmdbMovieResponse movie = omdbRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/").queryParam("t", title).build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new MovieNotFoundException("OMDb request failed: " + response.getStatusCode());
                })
                .body(OmdbMovieResponse.class);

        return verifyFound(movie);
    }

    // get a movie by its title, asking OMDb for the full plot text
    public OmdbMovieResponse getMoviePlotByName(String title) {
        OmdbMovieResponse movie = omdbRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/")
                        .queryParam("t", title)
                        .queryParam("plot", "full")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new MovieNotFoundException("OMDb request failed: " + response.getStatusCode());
                })
                .body(OmdbMovieResponse.class);

        return verifyFound(movie);
    }

    // OMDb answers HTTP 200 even when nothing matches, flagging it with "Response":"False".
    // This turns that case into a proper not-found.
    private OmdbMovieResponse verifyFound(OmdbMovieResponse movie) {
        if (movie == null || !"True".equalsIgnoreCase(movie.response())) {
            String reason = (movie != null && movie.error() != null) ? movie.error() : "No response from OMDb";
            throw new MovieNotFoundException(reason);
        }
        return movie;
    }

    // --- Service MEthods ---
    // create UserData if none available for userName
    public UserData createUser(String userName){
        if( userDataRepository.findById(userName).orElse(null) == null ){
            UserData newUD = UserData.builder()
                    .userName(userName)
                    .omdbMovieResponseList(new ArrayList<OmdbMovieResponse>())
                    .build();
            return this.saveUserData(newUD);
        }
        else{
            return null;
        }
    }

    // show UserData for username or create if not available
    public UserData showUserData(String username) {
        UserData uD = userDataRepository.findById(username).orElse(null);
        return uD != null ? uD : createUser(username);
    }

    //  add new FavouriteMovie to UserData
    public UserData addMovieToFavorites(String githubUsername, OmdbMovieResponse newMovie) {
        UserData existingUser = userDataRepository.findById(githubUsername)
                .orElseThrow(() -> new RuntimeException("User not found, though logged in!"));

        // returns copy of Record without status fields
        OmdbMovieResponse cleanedMovie = newMovie
                .withResponse(null)
                .withError(null);

        List<OmdbMovieResponse> currentList = existingUser.omdbMovieResponseList() != null
                ? existingUser.omdbMovieResponseList()
                : List.of();

        boolean alreadyExists = currentList.stream()
                .anyMatch(movie -> movie.imdbId() != null && movie.imdbId().equals(cleanedMovie.imdbId()));
        if (alreadyExists) {
            return existingUser;
        }

        List<OmdbMovieResponse> updatedList = new ArrayList<>(currentList);
        updatedList.add(cleanedMovie);

        UserData userWithNewFavorites = existingUser.withOmdbMovieResponseList(updatedList);
        return userDataRepository.save(userWithNewFavorites);
    }

    // return the current user's favourites (load-or-create so a fresh user gets an empty list)
    public List<OmdbMovieResponse> getFavorites(String githubUsername) {
        UserData user = showUserData(githubUsername);
        return user.omdbMovieResponseList() != null ? user.omdbMovieResponseList() : new ArrayList<>();
    }

    // remove a favourite by its imdbId and persist the shortened list
    public UserData removeMovieFromFavorites(String githubUsername, String imdbId) {
        UserData existingUser = userDataRepository.findById(githubUsername)
                .orElseThrow(() -> new RuntimeException("User not found, though logged in!"));

        List<OmdbMovieResponse> currentList = existingUser.omdbMovieResponseList() != null
                ? existingUser.omdbMovieResponseList()
                : List.of();

        List<OmdbMovieResponse> updatedList = currentList.stream()
                .filter(movie -> movie.imdbId() == null || !movie.imdbId().equals(imdbId))
                .toList();

        return userDataRepository.save(existingUser.withOmdbMovieResponseList(updatedList));
    }
    // END --- Service MEthods ---

    // --- CRUD Repository ---
    public UserData saveUserData(UserData userData) {
        if(userDataRepository.existsByUserName(userData.userName())){
            return null;
        }
        else{
            return userDataRepository.save(userData);
        }
    }
    // END --- CRUD Repository ---
}
