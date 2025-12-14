package com.example.tournaments.controllers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
            linkTo(methodOn(TournamentController.class).listTournaments(1, 20)).withRel("tournaments"),
             linkTo(methodOn(ParticipationController.class).listUpcomingTournaments()).withRel("upcomingTournaments"),
                linkTo(methodOn(UserProfileController.class).getAllUsers()).withRel("allUsers"),
                linkTo(methodOn(RootController.class).getRoot()).withRel("documentation").withHref("/swagger-ui.html")
        );
        return rootModel;
    }
}