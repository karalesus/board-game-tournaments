package com.example.tournaments.assemblers;

import com.example.tournamentsapi.dto.tournaments.TournamentResponse;
import com.example.tournamentsapi.endpoints.TournamentApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TournamentModelAssembler implements RepresentationModelAssembler<TournamentResponse, EntityModel<TournamentResponse>> {

    @Override
    public EntityModel<TournamentResponse> toModel(TournamentResponse tournament) {
        return EntityModel.of(tournament,
                linkTo(methodOn(TournamentApi.class).getTournament(tournament.getId())).withSelfRel(),
                linkTo(methodOn(TournamentApi.class).getParticipants(tournament.getId())).withRel("participants"),
                linkTo(methodOn(TournamentApi.class).getDrawResults(tournament.getId())).withRel("drawResults"),
                linkTo(methodOn(TournamentApi.class).postTournamentResults(tournament.getId(), null)).withRel("results"),
                linkTo(methodOn(TournamentApi.class).listTournaments(1, 20)).withRel("collection")
        );
    }
}