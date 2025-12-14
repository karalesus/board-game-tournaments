package com.example.tournaments.repositories.impl;

import com.example.tournaments.models.Participation;
import com.example.tournaments.repositories.BaseRepository;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.example.tournaments.repositories.ParticipationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ParticipationRepositoryImpl extends BaseRepository<Participation, UUID>  implements ParticipationRepository {

    public ParticipationRepositoryImpl() {
        super(Participation.class);
    }

    public List<Participation> findByTournamentId(UUID tournamentId) {
        var q = entityManager.createQuery("SELECT r FROM Participation r JOIN r.tournament t WHERE t.id = :tid", Participation.class);
        q.setParameter("tid", tournamentId);
        return q.getResultList();
    }

    public List<Participation> findByUserId(UUID userId) {
        var q = entityManager.createQuery("SELECT r FROM Participation r JOIN r.user u WHERE u.id = :uid", Participation.class);
        q.setParameter("uid", userId);
        return q.getResultList();
    }

    public List<Participation> findByUserAndTournament(UUID userId, UUID tournamentId) {
        TypedQuery<Participation> query = entityManager.createQuery(
                "SELECT r FROM Participation r " +
                        "WHERE r.user.id = :userId " +
                        "AND r.tournament.id = :tournamentId", Participation.class
        );
        query.setParameter("userId", userId);
        query.setParameter("tournamentId", tournamentId);
        return query.getResultList();
    }

    public Optional<Participation> findByTournamentAndUser(UUID tournamentId, UUID userId) {
        var q = entityManager.createQuery("SELECT r FROM Participation r WHERE r.tournament.id = :tid AND r.user.id = :uid", Participation.class)
                .setParameter("tid", tournamentId)
                .setParameter("uid", userId);
        var list = q.getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}