package com.example.tournaments.controllers;


import com.example.tournaments.assemblers.DrawResultModelAssembler;
import com.example.tournaments.assemblers.TournamentModelAssembler;
import com.example.tournaments.assemblers.TournamentResultModelAssembler;
import com.example.tournaments.assemblers.UserModelAssembler;
import com.example.tournaments.services.impl.TournamentServiceImpl;
import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.dto.tournaments.*;
import com.example.tournamentsapi.endpoints.TournamentApi;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TournamentController implements TournamentApi {

    private final TournamentServiceImpl tournamentService;
    private final TournamentModelAssembler tournamentAssembler;
    private final TournamentResultModelAssembler resultAssembler;

    @Autowired
    public TournamentController(TournamentServiceImpl tournamentService, TournamentModelAssembler tournamentAssembler, TournamentResultModelAssembler resultAssembler) {
        this.tournamentService = tournamentService;
        this.tournamentAssembler = tournamentAssembler;
        this.resultAssembler = resultAssembler;
    }

    @Override
    public ResponseEntity<EntityModel<TournamentResponse>> createTournament(@Valid TournamentRequest request) {
        ResponseEntity<EntityModel<TournamentResponse>> response = tournamentService.createTournament(request);
        return ResponseEntity.created(response.getHeaders().getLocation())
                .body(tournamentAssembler.toModel(response.getBody().getContent()));
    }

    @Override
    public ResponseEntity<EntityModel<TournamentResponse>> updateTournament(UUID id, @Valid TournamentUpdateRequest request) {
        ResponseEntity<EntityModel<TournamentResponse>> response = tournamentService.updateTournament(id, request);
        return ResponseEntity.ok(tournamentAssembler.toModel(response.getBody().getContent()));
    }

    @Override
    public StatusResponse deleteTournament(UUID id) {
        return tournamentService.deleteTournament(id);
    }

    @Override
    public EntityModel<TournamentResponse> getTournament(UUID id) {
        return tournamentAssembler.toModel(tournamentService.getTournamentById(id).getContent());
    }

    @Override
    public PagedModel<EntityModel<TournamentResponse>> listTournaments(int page, int size) {
        return tournamentService.listTournaments(page, size);
    }

    @Override
    public CollectionModel<EntityModel<UserResponse>> getParticipants(UUID id) {
        return tournamentService.getParticipants(id);
    }

    @Override
    public StatusResponse draw(UUID id) {
        return tournamentService.draw(id);
    }

    @Override
    public CollectionModel<EntityModel<DrawResultResponse>> getDrawResults(UUID id) {
        return tournamentService.getDrawResults(id);
    }

    @Override
    public ResponseEntity<EntityModel<TournamentResultResponse>> postTournamentResults(UUID id, @Valid TournamentResultRequest request) {
        ResponseEntity<EntityModel<TournamentResultResponse>> response = tournamentService.postTournamentResults(id, request);
        return ResponseEntity.created(response.getHeaders().getLocation())
                .body(resultAssembler.toModel(response.getBody().getContent()));
    }

    @Override
    public EntityModel<TournamentResultResponse> getTournamentResults(UUID id) {
        return resultAssembler.toModel(tournamentService.getTournamentResults(id).getContent());
    }

}
