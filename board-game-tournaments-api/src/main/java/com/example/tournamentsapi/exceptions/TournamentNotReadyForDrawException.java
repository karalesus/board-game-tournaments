package com.example.tournamentsapi.exceptions;
import com.example.tournamentsapi.enums.TournamentStatus;

public class TournamentNotReadyForDrawException extends RuntimeException {
    public TournamentNotReadyForDrawException(TournamentStatus currentStatus) {
        super(String.format(
            "Жеребьёвка невозможна: турнир в статусе %s. " +
            "Требуется ACTIVE или REGISTRATION_CLOSED",
            currentStatus
        ));
    }
}