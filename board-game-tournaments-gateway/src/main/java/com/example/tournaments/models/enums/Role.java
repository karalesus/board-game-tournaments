package com.example.tournaments.models.enums;

public enum Role {
    USER("Пользователь"),
    ADMIN("Администратор");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}