package com.example.tournamentsapi.exceptions;

public class TournamentFullException extends RuntimeException {
    public TournamentFullException() {
        super("Турнир переполнен: достигнуто максимальное количество участников");
    }

    public TournamentFullException(int registered, int max) {
        super(String.format("Турнир переполнен: %d из %d мест занято", registered, max));
    }
}