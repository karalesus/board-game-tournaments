package com.example.tournaments.repositories.impl;

import com.example.tournaments.models.Tournament;
import com.example.tournaments.repositories.BaseRepository;
import com.example.tournaments.repositories.TournamentRepository;
import com.example.tournamentsapi.enums.TournamentStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TournamentRepositoryImpl extends BaseRepository<Tournament, UUID> implements TournamentRepository {

    public TournamentRepositoryImpl() {
        super(Tournament.class);
    }

    public List<Tournament> findAll(int page, int size) {
        return entityManager.createQuery("SELECT t FROM Tournament t ORDER BY t.startTime ASC", Tournament.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Optional<Tournament> findByName(String name) {
        return super.findByName(name);
    }

    public List<Tournament> findByGame(String game) {
        var q = entityManager.createQuery("SELECT t FROM Tournament t WHERE LOWER(t.game) = LOWER(:game) ORDER BY t.startTime", Tournament.class);
        q.setParameter("game", game);
        return q.getResultList();
    }

    public List<Tournament> findByStatus(TournamentStatus status) {
        return entityManager.createQuery(
                        "SELECT t FROM Tournament t WHERE t.status = :status", Tournament.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Tournament> getUpcomingTournaments(LocalDateTime now) {
        return entityManager.createQuery(
                        "SELECT t FROM Tournament t WHERE t.startTime > :now ORDER BY t.startTime ASC", Tournament.class)
                .setParameter("now", now)
                .getResultList();
    }

    public long countAll() {
        return entityManager.createQuery("SELECT COUNT(t) FROM Tournament t", Long.class)
                .getSingleResult();
    }
}
