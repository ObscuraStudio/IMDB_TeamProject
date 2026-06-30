package org.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One entry from OMDb's "Ratings" array,
 * e.g. {"Source": "Rotten Tomatoes", "Value": "87%"}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Rating(
        @JsonProperty("Source") String source,
        @JsonProperty("Value") String value
) {
}
