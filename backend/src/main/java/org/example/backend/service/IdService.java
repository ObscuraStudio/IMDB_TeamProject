package org.example.backend.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class IdService {

    private static String newID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String generateNewId() {
        return newID();
    }
}
