package com.example.tournamentsapi.dto.participations;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record ParticipationRequest(
    @NotNull UUID tournamentId,
    @NotNull UUID playerId
) {}
