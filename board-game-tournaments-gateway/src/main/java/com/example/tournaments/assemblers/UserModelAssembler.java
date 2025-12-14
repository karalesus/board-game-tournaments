package com.example.tournaments.assemblers;

import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.endpoints.UserProfileApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

    @Override
    public EntityModel<UserResponse> toModel(UserResponse user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserProfileApi.class).getUserProfile(user.getId())).withSelfRel(),
                linkTo(methodOn(UserProfileApi.class).listUserTournaments(user.getId())).withRel("registrations")
        );
    }

    @Override
    public CollectionModel<EntityModel<UserResponse>> toCollectionModel(Iterable<? extends UserResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(UserProfileApi.class).getUserProfile(null)).withRel("users"));
    }
}