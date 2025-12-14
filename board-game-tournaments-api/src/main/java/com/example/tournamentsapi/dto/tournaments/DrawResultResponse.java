package com.example.tournamentsapi.dto.tournaments;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


@Relation(collectionRelation = "drawResults", itemRelation = "drawResult")
public class DrawResultResponse extends RepresentationModel<DrawResultResponse> {
    private UUID userId;
    private int position;

    public DrawResultResponse(UUID userId, int position) {
        this.userId = userId;
        this.position = position;
    }

    public DrawResultResponse() {
    }

    public UUID getUserId() {
        return userId;
    }

    public int getPosition() {
        return position;
    }
}
