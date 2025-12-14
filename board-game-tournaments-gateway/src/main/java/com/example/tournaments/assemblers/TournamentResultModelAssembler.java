package com.example.tournaments.assemblers;

import com.example.tournamentsapi.dto.tournaments.TournamentResultResponse;
import com.example.tournamentsapi.endpoints.TournamentApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TournamentResultModelAssembler implements RepresentationModelAssembler<TournamentResultResponse, EntityModel<TournamentResultResponse>> {

    @Override
    public EntityModel<TournamentResultResponse> toModel(TournamentResultResponse result) {
        return EntityModel.of(result,
            linkTo(methodOn(TournamentApi.class).postTournamentResults(result.getTournamentId(), null)).withSelfRel(),
            linkTo(methodOn(TournamentApi.class).getTournament(result.getTournamentId())).withRel("tournament")
        );
    }

    @Override
    public CollectionModel<EntityModel<TournamentResultResponse>> toCollectionModel(Iterable<? extends TournamentResultResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
            .add(linkTo(methodOn(TournamentApi.class).postTournamentResults(null, null)).withRel("results"));
    }
}