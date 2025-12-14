//package com.example.tournaments.dto.tournament;
//
//import jakarta.validation.constraints.NotBlank;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//public class TournamentDTO {
//
//    private UUID id;
//
//    @NotBlank(message = "Название турнира не может быть пустым")
//    private String name;
//
//    @NotBlank(message = "Название игры не может быть пустым")
//    private String game;
//
//    private LocalDateTime createdAt;
//
//    private TournamentStatus status;
//
//    public TournamentDTO() {
//    }
//
//    public TournamentDTO(UUID id, String name, String game, LocalDateTime createdAt, TournamentStatus status) {
//        this.id = id;
//        this.name = name;
//        this.game = game;
//        this.createdAt = createdAt;
//        this.status = status;
//    }
//
//    public UUID getId() { return id; }
//    public void setId(UUID id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getGame() { return game; }
//    public void setGame(String game) { this.game = game; }
//
//    public LocalDateTime getCreatedAt() { return createdAt; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//
//    public TournamentStatus getStatus() { return status; }
//    public void setStatus(TournamentStatus status) { this.status = status; }
//
//    @Override
//    public String toString() {
//        return "TournamentDTO{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", game='" + game + '\'' +
//                ", createdAt=" + createdAt +
//                ", status=" + status +
//                '}';
//    }
//}
