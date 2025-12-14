package com.example.tournamentsapi.enums;

public enum TournamentStatus {
    ACTIVE("Активный"),
    FINISHED("Завершён"),
    DELETED("Удалён"),
    DRAWN("Распределен");

    private final String description;

    TournamentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
