package org.example;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record RegistrationCheckedEvent(
    UUID userId,
    UUID tournamentId,
    boolean available,
    String reason,
    LocalDateTime checkedAt
) implements Serializable {}