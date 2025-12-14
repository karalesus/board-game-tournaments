package com.example.tournamentsapi.exceptions;

public class DrawAlreadyPerformedException extends RuntimeException {
    public DrawAlreadyPerformedException() {
        super("Жеребьёвка уже была проведена");
    }
}