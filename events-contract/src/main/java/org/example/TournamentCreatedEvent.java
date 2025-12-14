package org.example;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record TournamentCreatedEvent(
        UUID tournamentId,
        String name,
        String game,
        String location,
        LocalDateTime startTime,
        Integer maxPlayers
) implements Serializable {}
