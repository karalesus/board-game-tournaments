package com.example.tournaments.services;

import com.example.tournamentsapi.dto.auth.UserRequest;
import com.example.tournamentsapi.dto.auth.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {


    List<UserResponse> getAllUsers();

    Optional<UserResponse> getUserById(UUID uuid);

    Optional<UserResponse> getUserByEmail(String email);

}
