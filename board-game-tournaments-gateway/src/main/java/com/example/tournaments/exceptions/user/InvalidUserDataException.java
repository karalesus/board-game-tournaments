package com.example.tournaments.exceptions.user;

public class InvalidUserDataException extends RuntimeException{

    public InvalidUserDataException(String message) {
        super(message);
    }
}
