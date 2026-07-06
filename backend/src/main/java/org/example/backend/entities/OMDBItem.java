package org.example.backend.entities;

import lombok.Builder;

@Builder
public record OMDBItem (
        String imdbId,
        String title,
        String plot
){
}
