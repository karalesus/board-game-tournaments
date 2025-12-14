package org.example;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerRegisteredEvent(
        UUID userId,
        UUID tournamentId,
        String game,
        String location,
        LocalDateTime registrationTime
) implements Serializable {}