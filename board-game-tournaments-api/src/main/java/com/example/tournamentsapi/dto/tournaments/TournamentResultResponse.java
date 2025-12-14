package com.example.tournamentsapi.dto.tournaments;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.util.Map;
import java.util.UUID;

@Relation(collectionRelation = "tournamentResults", itemRelation = "tournamentResult")
public class TournamentResultResponse extends RepresentationModel<TournamentResultResponse> {
    private final UUID tournamentId;
    private final Map<UUID, Integer> results;

    public TournamentResultResponse(UUID tournamentId, Map<UUID, Integer> results) {
        this.tournamentId = tournamentId;
        this.results = results;
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public Map<UUID, Integer> getResults() {
        return results;
    }
}