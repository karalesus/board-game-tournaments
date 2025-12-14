package com.example.tournaments.repositories;


import com.example.tournaments.models.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
