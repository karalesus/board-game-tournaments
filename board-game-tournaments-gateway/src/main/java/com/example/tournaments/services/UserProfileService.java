package com.example.tournaments.services;

import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentRequest;
import com.example.tournamentsapi.dto.tournaments.TournamentResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentUpdateRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserProfileService {

    EntityModel<UserResponse> getUserProfile(UUID userId);
    StatusResponse cancelTournamentRegistration(UUID userId, UUID tournamentId);
    CollectionModel<EntityModel<ParticipationResponse>> listUserTournaments(UUID userId);
    CollectionModel<EntityModel<UserResponse>> getAllUsers();
}
