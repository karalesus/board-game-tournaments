package com.example.tournaments.services;

import com.example.tournamentsapi.dto.participations.ParticipationRequest;
import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentResultRequest;
import com.example.tournamentsapi.dto.tournaments.TournamentResultResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


public interface ParticipationService {

    ResponseEntity<EntityModel<ParticipationResponse>> participate(ParticipationRequest request);
    CollectionModel<EntityModel<TournamentResponse>> listUpcomingTournaments();
}
