package com.example.tournamentsapi.exceptions;

public class NoParticipantsException extends RuntimeException {
    public NoParticipantsException() {
        super("Нет участников, записанных на этот турнир");
    }
}