package com.example.tournamentsapi.exceptions;

public class RegistrationNotAvailableException extends RuntimeException {
    public RegistrationNotAvailableException(String message) {
        super(message);
    }
}