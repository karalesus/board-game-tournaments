package org.example;

import java.io.Serializable;
import java.util.UUID;

public record TournamentDrawPerformedEvent(
        UUID tournamentId,
        String tournamentName
) implements Serializable {}