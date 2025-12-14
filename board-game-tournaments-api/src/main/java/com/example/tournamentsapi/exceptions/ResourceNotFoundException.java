package com.example.tournamentsapi.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Object id) {
        super(String.format("%s с ID %s не найден", resource, id));
    }

    public ResourceNotFoundException(String resource, UUID id) {
        this(resource, id.toString());
    }
}