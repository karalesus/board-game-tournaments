package com.example.tournaments.repositories;
import com.example.tournaments.models.Participation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository {

    List<Participation> findByTournamentId(UUID tournamentId);

    List<Participation> findByUserId(UUID userId);

    Optional<Participation> findByTournamentAndUser(UUID tournamentId, UUID userId);
}
