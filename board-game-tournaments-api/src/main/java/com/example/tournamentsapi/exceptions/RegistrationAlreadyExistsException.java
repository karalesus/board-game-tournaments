package com.example.tournamentsapi.exceptions;

import java.util.UUID;

public class RegistrationAlreadyExistsException extends RuntimeException {
    public RegistrationAlreadyExistsException(UUID userId, UUID tournamentId) {
        super(String.format("Пользователь %s уже зарегистрирован на турнир %s", userId, tournamentId));
    }
}