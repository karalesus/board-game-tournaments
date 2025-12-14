package org.example;

import java.io.Serializable;
import java.util.UUID;

public record TournamentDeletedEvent(
        UUID tournamentId
) implements Serializable {}