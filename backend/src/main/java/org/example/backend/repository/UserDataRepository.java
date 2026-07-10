package org.example.backend.repository;

import org.example.backend.entities.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends MongoRepository<UserData, String> {

    boolean existsByUserName(String username);

}
