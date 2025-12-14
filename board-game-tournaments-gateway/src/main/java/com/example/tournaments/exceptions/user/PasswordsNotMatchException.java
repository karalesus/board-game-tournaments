package com.example.tournaments.exceptions.user;

public class PasswordsNotMatchException extends RuntimeException{

    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
