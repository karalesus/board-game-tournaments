package com.example.tournaments.assemblers;

import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.endpoints.ParticipationApi;
import com.example.tournamentsapi.endpoints.TournamentApi;
import com.example.tournamentsapi.endpoints.UserProfileApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ParticipationModelAssembler implements RepresentationModelAssembler<ParticipationResponse, EntityModel<ParticipationResponse>> {

    @Override
    public EntityModel<ParticipationResponse> toModel(ParticipationResponse participation) {
        return EntityModel.of(participation,
                linkTo(methodOn(ParticipationApi.class).participate(null)).withSelfRel(), // Нет прямого get, используем participate как self
                linkTo(methodOn(TournamentApi.class).getTournament(participation.getTournamentId())).withRel("tournament"),
                linkTo(methodOn(UserProfileApi.class).getUserProfile(participation.getPlayerId())).withRel("player")
        );
    }
}