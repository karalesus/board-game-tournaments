package com.example.tournaments.assemblers;

import com.example.tournamentsapi.dto.tournaments.DrawResultResponse;
import com.example.tournamentsapi.endpoints.TournamentApi;
import com.example.tournamentsapi.endpoints.UserProfileApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DrawResultModelAssembler implements RepresentationModelAssembler<DrawResultResponse, EntityModel<DrawResultResponse>> {

    @Override
    public EntityModel<DrawResultResponse> toModel(DrawResultResponse drawResult) {
        return EntityModel.of(drawResult,
                linkTo(methodOn(TournamentApi.class).getDrawResults(null)).withSelfRel(), // Условный self
                linkTo(methodOn(UserProfileApi.class).getUserProfile(drawResult.getUserId())).withRel("player")
        );
    }

    @Override
    public CollectionModel<EntityModel<DrawResultResponse>> toCollectionModel(Iterable<? extends DrawResultResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(TournamentApi.class).getDrawResults(null)).withSelfRel());
    }
}