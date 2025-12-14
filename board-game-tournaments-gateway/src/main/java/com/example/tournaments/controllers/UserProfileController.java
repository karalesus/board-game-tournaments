package com.example.tournaments.controllers;

import com.example.tournaments.assemblers.UserModelAssembler;
import com.example.tournaments.services.impl.UserProfileServiceImpl;
import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.endpoints.UserProfileApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@RestController
public class UserProfileController implements UserProfileApi {

    private final UserProfileServiceImpl userProfileService;
    private final UserModelAssembler assembler;

    public UserProfileController(UserProfileServiceImpl userProfileService, UserModelAssembler assembler) {
        this.userProfileService = userProfileService;
        this.assembler = assembler;
    }

    @Override
    public EntityModel<UserResponse> getUserProfile(UUID userId) {
        return assembler.toModel(Objects.requireNonNull(userProfileService.getUserProfile(userId).getContent()));
    }

    @Override
    public StatusResponse cancelTournamentRegistration(UUID userId, UUID tournamentId) {
        return userProfileService.cancelTournamentRegistration(userId, tournamentId);
    }

    @Override
    public CollectionModel<EntityModel<ParticipationResponse>> listUserTournaments(UUID userId) {
        return userProfileService.listUserTournaments(userId);
    }

    @Override
    public CollectionModel<EntityModel<UserResponse>> getAllUsers() {
        return userProfileService.getAllUsers();
    }
}
