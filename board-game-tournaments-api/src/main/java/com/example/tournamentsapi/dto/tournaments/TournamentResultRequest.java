package com.example.tournamentsapi.dto.tournaments;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record TournamentResultRequest(
        @NotNull Map<UUID, Integer> results
) {}