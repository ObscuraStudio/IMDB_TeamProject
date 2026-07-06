package org.example.backend.entities;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document("UserData")
public record UserData(
        @Id
        String id,
        String userName,
        @With
        List<OMDBItem> omdbItemList
) {
}
