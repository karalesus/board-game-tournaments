package com.example.tournaments.services;

import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.dto.tournaments.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface TournamentService {

    ResponseEntity<EntityModel<TournamentResponse>> createTournament(TournamentRequest request);

    ResponseEntity<EntityModel<TournamentResponse>> updateTournament(UUID id, TournamentUpdateRequest request);

    EntityModel<TournamentResponse>  getTournamentById(UUID id);

    List<TournamentResponse> getAllTournaments();

    StatusResponse deleteTournament(UUID id);

    PagedModel<EntityModel<TournamentResponse>> listTournaments(int page, int size);

    StatusResponse draw(UUID tournamentId);

    CollectionModel<EntityModel<DrawResultResponse>> getDrawResults(UUID tournamentId);

    CollectionModel<EntityModel<UserResponse>> getParticipants(UUID tournamentId);

    ResponseEntity<EntityModel<TournamentResultResponse>> postTournamentResults(UUID tournamentId, TournamentResultRequest request);
    EntityModel<TournamentResultResponse> getTournamentResults(UUID tournamentId);

}
