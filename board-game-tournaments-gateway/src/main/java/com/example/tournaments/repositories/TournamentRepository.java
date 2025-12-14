package com.example.tournaments.repositories;


import com.example.tournaments.models.Tournament;
import com.example.tournamentsapi.enums.TournamentStatus;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository {

    Optional<Tournament> findByName(String name);
    List<Tournament> findByGame(String game);
    List<Tournament> findByStatus(TournamentStatus status);
}
