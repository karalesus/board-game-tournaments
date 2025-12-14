package com.example.tournaments.controllers;


import com.example.tournaments.assemblers.ParticipationModelAssembler;
import com.example.tournaments.services.impl.ParticipationServiceImpl;
import com.example.tournamentsapi.dto.participations.ParticipationRequest;
import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentResponse;
import com.example.tournamentsapi.endpoints.ParticipationApi;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ParticipationController implements ParticipationApi {

    private final ParticipationServiceImpl participationService;
    private final ParticipationModelAssembler assembler;

    @Autowired
    public ParticipationController(ParticipationServiceImpl participationService, ParticipationModelAssembler assembler) {
        this.participationService = participationService;
        this.assembler = assembler;
    }

    @Override
    public ResponseEntity<EntityModel<ParticipationResponse>> participate(@Valid ParticipationRequest request) {
        ResponseEntity<EntityModel<ParticipationResponse>> response = participationService.participate(request);
        return ResponseEntity.created(response.getHeaders().getLocation())
                .body(assembler.toModel(response.getBody().getContent()));
    }

    @Override
    public CollectionModel<EntityModel<TournamentResponse>> listUpcomingTournaments() {
        return participationService.listUpcomingTournaments();
    }
}