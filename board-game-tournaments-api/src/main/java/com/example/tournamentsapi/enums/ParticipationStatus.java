package com.example.tournamentsapi.enums;

public enum ParticipationStatus {
    REGISTERED("Зарегистрирован"),
    CANCELLED("Отменено"),
    DRAWN("Распределен"),
    FINISHED("Завершено");

    private final String description;

    ParticipationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}