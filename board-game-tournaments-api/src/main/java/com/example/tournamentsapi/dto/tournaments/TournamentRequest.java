package com.example.tournamentsapi.dto.tournaments;

import com.example.tournamentsapi.enums.TournamentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record TournamentRequest(
        @NotBlank(message = "Название турнира не может быть пустым")
        String name,

        @NotBlank(message = "Игра обязательна")
        String game,

        @Future(message = "Дата начала должна быть в будущем")
        LocalDateTime startTime,

        @Positive(message = "Максимальное количество игроков должно быть положительным")
        int maxPlayers,

        String description,
        String location,
        TournamentStatus status
) {}
