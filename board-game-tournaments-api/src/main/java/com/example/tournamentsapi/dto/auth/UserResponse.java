package com.example.tournamentsapi.dto.auth;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.util.UUID;

@Relation(collectionRelation = "users", itemRelation = "user")
public class UserResponse extends RepresentationModel<UserResponse> {
    private  UUID id;
    private  String name;

    public UserResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    protected UserResponse() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}