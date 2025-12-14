package org.example;

import java.io.Serializable;
import java.util.UUID;

public record PlayerCancelledEvent(
        UUID userId,
        UUID tournamentId
) implements Serializable {}