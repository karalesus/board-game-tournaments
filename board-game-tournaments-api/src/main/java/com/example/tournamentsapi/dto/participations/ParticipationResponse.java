package com.example.tournamentsapi.dto.participations;

import com.example.tournamentsapi.enums.ParticipationStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Relation(collectionRelation = "participations", itemRelation = "participation")
public class ParticipationResponse extends RepresentationModel<ParticipationResponse> {
    private final UUID participationId;
    private final UUID playerId;
    private final UUID tournamentId;
    private final ParticipationStatus status;
    private final LocalDateTime createdAt;

    public ParticipationResponse(UUID participationId, UUID playerId, UUID tournamentId, ParticipationStatus status, LocalDateTime createdAt) {
        this.participationId = participationId;
        this.playerId = playerId;
        this.tournamentId = tournamentId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getParticipationId() {
        return participationId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public ParticipationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ParticipationResponse that = (ParticipationResponse) o;
        return Objects.equals(participationId, that.participationId) && Objects.equals(playerId, that.playerId) && Objects.equals(tournamentId, that.tournamentId) && status == that.status && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), participationId, playerId, tournamentId, status, createdAt);
    }
}
