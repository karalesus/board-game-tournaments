package com.example.tournaments.services.impl;

import com.example.tournaments.assemblers.UserModelAssembler;
import com.example.tournaments.cfg.RabbitMQConfig;
import com.example.tournaments.models.Participation;
import com.example.tournaments.models.Tournament;
import com.example.tournaments.models.User;
import com.example.tournaments.repositories.impl.ParticipationRepositoryImpl;
import com.example.tournaments.repositories.impl.TournamentRepositoryImpl;
import com.example.tournaments.repositories.impl.UserRepositoryImpl;
import com.example.tournaments.services.UserProfileService;
import com.example.tournamentsapi.dto.auth.UserResponse;

import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentResponse;
import com.example.tournamentsapi.enums.ParticipationStatus;
import com.example.tournamentsapi.exceptions.ResourceNotFoundException;
import org.example.PlayerCancelledEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final ParticipationRepositoryImpl participationRepository;
    private final TournamentRepositoryImpl tournamentRepository;
    private final UserRepositoryImpl userRepository;
    private final UserModelAssembler userModelAssembler;
    private final RabbitTemplate rabbitTemplate;

    public UserProfileServiceImpl(ParticipationRepositoryImpl participationRepository, TournamentRepositoryImpl tournamentRepository,
                                  UserRepositoryImpl userRepository, UserModelAssembler userModelAssembler, RabbitTemplate rabbitTemplate) {
        this.participationRepository = participationRepository;
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.userModelAssembler = userModelAssembler;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public EntityModel<UserResponse> getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        return EntityModel.of(new UserResponse(user.getId(), user.getName()));
    }


    @Override
    public StatusResponse cancelTournamentRegistration(UUID userId, UUID tournamentId) {
        Participation participation = participationRepository.findByTournamentId(tournamentId)
                .stream()
                .filter(p -> p.getUser().getId().equals(userId) && p.getStatus() == ParticipationStatus.REGISTERED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Нет такой записи на турнир!"));
        participation.setStatus(ParticipationStatus.CANCELLED);
        participationRepository.save(participation);

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", tournamentId));
        tournament.setRegisteredPlayers(tournament.getRegisteredPlayers() - 1);
        tournamentRepository.save(tournament);

        PlayerCancelledEvent event = new PlayerCancelledEvent(userId, tournamentId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_PLAYER_CANCELLED,
                event
        );
        return new StatusResponse("success", null);
    }

    @Override
    public CollectionModel<EntityModel<ParticipationResponse>> listUserTournaments(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        List<ParticipationResponse> participations = participationRepository.findByUserId(user.getId())
                .stream()
                .map(this::convertParticipationToParticipationResponse)
                .collect(Collectors.toList());
        return CollectionModel.of(participations.stream().map(EntityModel::of).collect(Collectors.toList()));
    }

    @Override
    public CollectionModel<EntityModel<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<EntityModel<UserResponse>> userModels = users.stream()
                .map(user -> {
                    UserResponse response = convertUserToUserResponse(user);
                    return userModelAssembler.toModel(response);
                })
                .collect(Collectors.toList());
        return CollectionModel.of(userModels);
    }

    private UserResponse convertUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse(user.getId(), user.getName());
        return userResponse;
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
