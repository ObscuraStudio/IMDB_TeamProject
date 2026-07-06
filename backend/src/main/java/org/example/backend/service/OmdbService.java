package org.example.backend.service;

import org.example.backend.dto.OmdbMovieResponse;
import org.example.backend.entities.OMDBItem;
import org.example.backend.entities.UserData;
import org.example.backend.exception.MovieNotFoundException;
import org.example.backend.repository.UserDataRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;

@Service
public class OmdbService {

    private final UserDataRepository userDataRepository;

    private final IdService idService;

    private final RestClient omdbRestClient;

    public OmdbService(RestClient omdbRestClient, UserDataRepository userDataRepository, IdService idService) {
        this.omdbRestClient = omdbRestClient;
        this.userDataRepository = userDataRepository;
        this.idService = idService;
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
    public boolean createUser(String userName){
        if( getUserData(userName) == null ){
            String newID = this.idService.generateNewId();
            UserData newUD = UserData.builder()
                    .id(newID)
                    .userName(userName)
                    .omdbItemList(new ArrayList<OMDBItem>())
                    .build();
            this.saveUserData(newUD);
            return true;
        }
        else{
            return false;
        }
    }

    public UserData showUserData(String username) {
        return getUserData(username);
    }
    // usw. ...
    // END --- Service MEthods ---

    // --- CRUD Repository ---
    public UserData getUserData(String username) {
        if(userDataRepository.existsByUserName(username)){
            return userDataRepository.getUserDataByUserName(username);
        }
        return null;
    }

    public UserData saveUserData(UserData userData) {
        if(userDataRepository.existsByUserName(userData.userName())){
            return null;
        }
        else{
            return userDataRepository.save(userData);
        }
    }

    public UserData updateUserData(UserData userData) {
        UserData uD = userDataRepository.getUserDataByUserName(userData.userName());
        UserData updatedUD = uD.withOmdbItemList(userData.omdbItemList());
        return userDataRepository.save(updatedUD);
    }

    public void deleteUserData(UserData userData) {
        if(userDataRepository.existsByUserName(userData.userName())){
            userDataRepository.delete(userData);
        }
    }

    // END --- CRUD Repository ---
}
