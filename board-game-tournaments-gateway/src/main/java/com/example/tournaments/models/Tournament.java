package com.example.tournaments.models;

import com.example.tournamentsapi.enums.TournamentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tournaments")
public class Tournament extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String game;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "max_players", nullable = false)
    private Integer maxPlayers;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String location;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "registered_players", nullable = false)
    private int registeredPlayers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TournamentStatus status;

    protected Tournament() {}

    public Tournament(String name,
                      String game,
                      LocalDateTime startTime,
                      Integer maxPlayers,
                      String description,
                      String location,
                      LocalDateTime createdAt,
                      int registeredPlayers,
                      TournamentStatus status) {
        this.name = name;
        this.game = game;
        this.startTime = startTime;
        this.maxPlayers = maxPlayers;
        this.description = description;
        this.location = location;
        this.createdAt = createdAt;
        this.registeredPlayers = registeredPlayers;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGame() { return game; }
    public void setGame(String game) { this.game = game; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public Integer getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getRegisteredPlayers() { return registeredPlayers; }
    public void setRegisteredPlayers(int registeredPlayers) { this.registeredPlayers = registeredPlayers; }

    public TournamentStatus getStatus() { return status; }
    public void setStatus(TournamentStatus status) { this.status = status; }
}
