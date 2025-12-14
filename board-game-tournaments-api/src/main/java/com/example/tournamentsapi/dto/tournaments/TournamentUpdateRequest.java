package com.example.tournamentsapi.dto.tournaments;

import com.example.tournamentsapi.enums.TournamentStatus;

import java.time.LocalDateTime;

public record TournamentUpdateRequest(
        String name,
        String game,
        LocalDateTime startTime,
        Integer maxPlayers,
        String description,
        String location,
        TournamentStatus status
) {}