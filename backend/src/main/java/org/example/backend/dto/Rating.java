package org.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * One entry from OMDb's "Ratings" array,
 * e.g. {"Source": "Rotten Tomatoes", "Value": "87%"}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Rating(
        @JsonProperty("Source") @Field("Source") String source,
        @JsonProperty("Value") @Field("Value") String value
) {
    @JsonCreator
    public Rating {
        // Compact canonical constructor kept only to attach @JsonCreator, which
        // tells Jackson how to deserialize this record. No validation is needed.
    }
}
