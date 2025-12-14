package com.example.tournaments.models;


import com.example.tournamentsapi.enums.ParticipationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participations")
public class Participation extends BaseEntity {

    private User user;

    private Tournament tournament;

    private ParticipationStatus status;

    private LocalDateTime createdAt;

    private Integer position;
    private Integer place;

    protected Participation() {}

    public Participation(User user, Tournament tournament, ParticipationStatus status, LocalDateTime createdAt, Integer position, Integer place) {
        this.user = user;
        this.tournament = tournament;
        this.status = status;
        this.createdAt = createdAt;
        this.position = position;
        this.place = place;
    }

    public static Participation create(User user, Tournament tournament, ParticipationStatus status) {
        Participation r = new Participation();
        r.user = user;
        r.tournament = tournament;
        r.status = status;
        r.createdAt = LocalDateTime.now();
        r.position = null;
        r.place = null;
        return r;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false, columnDefinition = "uuid")
    public Tournament getTournament() { return tournament; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }

    @Enumerated(EnumType.STRING)
    public ParticipationStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipationStatus status) {
        this.status = status;
    }

    @Column(name = "created_at", nullable = false)
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Column(name = "position")
    public Integer getPosition() {
        return position;
    }

    @Column(name = "place")
    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
