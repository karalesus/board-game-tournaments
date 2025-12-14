package com.example.tournamentsapi.dto.tournaments;

import com.example.tournamentsapi.enums.TournamentStatus;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Relation(collectionRelation = "tournaments", itemRelation = "tournament")
public class TournamentResponse extends RepresentationModel<TournamentResponse> {
    private final UUID id;
    private final String name;
    private final String game;
    private final LocalDateTime startTime;
    private final int maxPlayers;
    private final int registeredPlayers;
    private final String description;
    private final String location;
    private final LocalDateTime createdAt;
    private final TournamentStatus status;

    public TournamentResponse(UUID id, String name, String game, LocalDateTime startTime, int maxPlayers, int registeredPlayers, String description, String location, LocalDateTime createdAt, TournamentStatus status) {
        this.id = id;
        this.name = name;
        this.game = game;
        this.startTime = startTime;
        this.maxPlayers = maxPlayers;
        this.registeredPlayers = registeredPlayers;
        this.description = description;
        this.location = location;
        this.createdAt = createdAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGame() {
        return game;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getRegisteredPlayers() {
        return registeredPlayers;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TournamentResponse that = (TournamentResponse) o;
        return maxPlayers == that.maxPlayers && registeredPlayers == that.registeredPlayers && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(game, that.game) && Objects.equals(startTime, that.startTime) && Objects.equals(description, that.description) && Objects.equals(location, that.location) && Objects.equals(createdAt, that.createdAt) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, game, startTime, maxPlayers, registeredPlayers, description, location, createdAt, status);
    }
}