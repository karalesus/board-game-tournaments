package org.example;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public record TournamentResultsPostedEvent(
        UUID tournamentId,
        Map<UUID, Integer> results
) implements Serializable {}