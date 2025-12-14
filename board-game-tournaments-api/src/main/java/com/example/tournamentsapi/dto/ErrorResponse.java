package com.example.tournamentsapi.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
    String errorCode,
    String message,
    LocalDateTime timestamp
) {}