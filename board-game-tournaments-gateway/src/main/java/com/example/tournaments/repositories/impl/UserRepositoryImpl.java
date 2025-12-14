package com.example.tournaments.repositories.impl;

import com.example.tournaments.models.User;
import com.example.tournaments.repositories.BaseRepository;
import com.example.tournaments.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, UUID> implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    public Optional<User> findByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }
}
