package com.example.tournaments.services.impl;

import com.example.RegistrationAvailabilityRequest;
import com.example.RegistrationAvailabilityResponse;
import com.example.ScheduleServiceGrpc;
import com.example.UserTournament;
import com.example.tournaments.cfg.RabbitMQConfig;
import com.example.tournaments.models.Participation;
import com.example.tournaments.models.Tournament;
import com.example.tournaments.models.User;
import com.example.tournamentsapi.dto.participations.ParticipationRequest;
import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentResponse;
import com.example.tournaments.repositories.impl.ParticipationRepositoryImpl;
import com.example.tournaments.repositories.impl.TournamentRepositoryImpl;
import com.example.tournaments.repositories.impl.UserRepositoryImpl;
import com.example.tournaments.services.ParticipationService;

import com.example.tournamentsapi.enums.ParticipationStatus;
import com.example.tournamentsapi.enums.TournamentStatus;
import com.example.tournamentsapi.exceptions.RegistrationAlreadyExistsException;
import com.example.tournamentsapi.exceptions.RegistrationNotAvailableException;
import com.example.tournamentsapi.exceptions.ResourceNotFoundException;
import com.example.tournamentsapi.exceptions.TournamentFullException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final UserRepositoryImpl userRepository;
    private final TournamentRepositoryImpl tournamentRepository;
    private final ParticipationRepositoryImpl participationRepository;
    private final RabbitTemplate rabbitTemplate;

    @GrpcClient("schedule-service")
    private ScheduleServiceGrpc.ScheduleServiceBlockingStub scheduleServiceStub;

    @Autowired
    public ParticipationServiceImpl(UserRepositoryImpl userRepository, TournamentRepositoryImpl tournamentRepository, ParticipationRepositoryImpl participationRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
        this.participationRepository = participationRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public ResponseEntity<EntityModel<ParticipationResponse>> participate(ParticipationRequest request) {
        Tournament tournament = tournamentRepository.findById(request.tournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", request.tournamentId()));
        User user = userRepository.findById(request.playerId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", request.playerId()));
        if (tournament.getRegisteredPlayers() >= tournament.getMaxPlayers()) {
            throw new TournamentFullException(tournament.getRegisteredPlayers(), tournament.getMaxPlayers());
        }

        List<Tournament> userTournaments = participationRepository.findByUserId(request.playerId())
                .stream()
                .filter(p -> p.getStatus() == ParticipationStatus.REGISTERED)
                .map(p -> tournamentRepository.findById(p.getTournament().getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<UserTournament> existingTournaments = userTournaments.stream()
                .map(t -> {
                    // Предполагаем, что турнир длится 2 часа (можно вычислить из данных)
                    LocalDateTime endTime = t.getStartTime().plusHours(2);
                    return UserTournament.newBuilder()
                            .setTournamentId(t.getId().toString())
                            .setStartTime(t.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                            .setEndTime(endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                            .build();
                })
                .collect(Collectors.toList());

        LocalDateTime tournamentEnd = tournament.getStartTime().plusHours(2); // или из данных турнира
        RegistrationAvailabilityRequest grpcRequest = RegistrationAvailabilityRequest.newBuilder()
                .setUserId(request.playerId().toString())
                .setTournamentId(request.tournamentId().toString())
                .setStartTime(tournament.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setEndTime(tournamentEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setCurrentRegistered(tournament.getRegisteredPlayers())
                .setMaxPlayers(tournament.getMaxPlayers())
                .addAllExistingTournaments(existingTournaments)
                .build();

        RegistrationAvailabilityResponse grpcResponse;
        try {
            grpcResponse = scheduleServiceStub.checkRegistrationAvailability(grpcRequest);

            if (!grpcResponse.getAvailable()) {
                throw new RegistrationNotAvailableException(grpcResponse.getReason());
            }
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Сервис проверки расписания недоступен: " + e.getStatus().getDescription(), e);
        }

        Participation participation = new Participation(
                user,
                tournament,
                ParticipationStatus.REGISTERED,
                LocalDateTime.now(),
                null,
                null);
        participationRepository.save(participation);
        tournament.setRegisteredPlayers(tournament.getRegisteredPlayers() + 1);
        tournamentRepository.save(tournament);

        PlayerRegisteredEvent event = new PlayerRegisteredEvent(
                user.getId(),
                tournament.getId(),
                tournament.getGame(),
                tournament.getLocation(),
                LocalDateTime.now()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_PLAYER_REGISTERED,
                event
        );
//        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", event);

        ParticipationResponse response = convertParticipationToParticipationResponse(participation);
        return ResponseEntity.created(URI.create("/api/tournaments/participation/participate"))
                .body(EntityModel.of(response));
    }

    @Override
    public CollectionModel<EntityModel<TournamentResponse>> listUpcomingTournaments() {
        List<Tournament> upcoming = tournamentRepository.findAll()
                .stream()
                .filter(t -> t.getStatus() == TournamentStatus.ACTIVE && t.getStartTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        return CollectionModel.of(upcoming.stream()
                .map(t -> EntityModel.of(convertTournamentToTournamentResponse(t)))
                .collect(Collectors.toList()));
    }

    private TournamentResponse convertTournamentToTournamentResponse(Tournament tournament) {
        TournamentResponse response = new TournamentResponse(tournament.getId(),
                tournament.getName(),
                tournament.getGame(),
                tournament.getStartTime(),
                tournament.getMaxPlayers(),
                tournament.getRegisteredPlayers(),
                tournament.getDescription(),
                tournament.getLocation(),
                tournament.getCreatedAt(),
                tournament.getStatus());
        return response;
    }

    private ParticipationResponse convertParticipationToParticipationResponse(Participation participation) {
        ParticipationResponse participationResponse = new ParticipationResponse(
                participation.getId(),
                participation.getUser().getId(),
                participation.getTournament().getId(),
                participation.getStatus(),
                participation.getCreatedAt()
        );
        return participationResponse;
    }
}
