package org.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 *
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OmdbMovieResponse(
        @JsonProperty("Title") String title,
        @JsonProperty("Year") String year,
        @JsonProperty("Rated") String rated,
        @JsonProperty("Released") String released,
        @JsonProperty("Runtime") String runtime,
        @JsonProperty("Genre") String genre,
        @JsonProperty("Director") String director,
        @JsonProperty("Writer") String writer,
        @JsonProperty("Actors") String actors,
        @JsonProperty("Plot") String plot,
        @JsonProperty("Language") String language,
        @JsonProperty("Country") String country,
        @JsonProperty("Awards") String awards,
        @JsonProperty("Poster") String poster,
        @Field("Ratings")
        @JsonProperty("Ratings") List<Rating> ratings,
        @JsonProperty("Metascore") String metascore,
        @JsonProperty("imdbRating") String imdbRating,
        @JsonProperty("imdbVotes") String imdbVotes,
        @JsonProperty("imdbID") String imdbId,
        @JsonProperty("Type") String type,
        @JsonProperty("BoxOffice") String boxOffice,

        // OMDb status fields for later? (Exceptions?)
        @With
        @JsonProperty("Response") String response,
        @With
        @JsonProperty("Error") String error
) {
}
