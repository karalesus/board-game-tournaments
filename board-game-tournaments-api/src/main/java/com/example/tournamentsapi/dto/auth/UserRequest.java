package com.example.tournamentsapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Имя пользователя не может быть пустым")
        String username,

        @Email(message = "Некорректный email")
        String email,

        @Size(min = 6, message = "Пароль должен быть не меньше 6 символов")
        String password
) {}
