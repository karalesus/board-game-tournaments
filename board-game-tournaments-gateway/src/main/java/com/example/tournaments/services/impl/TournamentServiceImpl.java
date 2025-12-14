package com.example.tournaments.services.impl;

import com.example.tournaments.assemblers.TournamentModelAssembler;
import com.example.tournaments.assemblers.TournamentResultModelAssembler;
import com.example.tournaments.cfg.RabbitMQConfig;
import com.example.tournaments.models.Participation;
import com.example.tournaments.models.Tournament;
import com.example.tournaments.repositories.impl.ParticipationRepositoryImpl;
import com.example.tournaments.repositories.impl.TournamentRepositoryImpl;
import com.example.tournaments.services.TournamentService;
import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.dto.tournaments.*;
import com.example.tournamentsapi.enums.ParticipationStatus;
import com.example.tournamentsapi.enums.TournamentStatus;
import com.example.tournamentsapi.exceptions.ResourceNotFoundException;
import org.example.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepositoryImpl tournamentRepository;
    private final ParticipationRepositoryImpl participationRepository;
    private final TournamentResultModelAssembler resultModelAssembler;
    private final TournamentModelAssembler tournamentModelAssembler;
    private final TournamentResultModelAssembler tournamentResultModelAssembler;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TournamentServiceImpl(TournamentRepositoryImpl tournamentRepository,
                                 ParticipationRepositoryImpl participationRepository, TournamentResultModelAssembler resultModelAssembler, TournamentModelAssembler tournamentModelAssembler, TournamentResultModelAssembler tournamentResultModelAssembler, RabbitTemplate rabbitTemplate) {
        this.tournamentRepository = tournamentRepository;
        this.participationRepository = participationRepository;
        this.resultModelAssembler = resultModelAssembler;
        this.tournamentModelAssembler = tournamentModelAssembler;
        this.tournamentResultModelAssembler = tournamentResultModelAssembler;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public ResponseEntity<EntityModel<TournamentResponse>> createTournament(TournamentRequest request) {
        Tournament tournament = new Tournament(
                request.name(),
                request.game(),
                request.startTime(),
                request.maxPlayers(),
                request.description(),
                request.location(),
                LocalDateTime.now(),
                0,
                request.status() != null ? request.status() : TournamentStatus.ACTIVE
        );
        Tournament saved = tournamentRepository.save(tournament);

        TournamentCreatedEvent event = new TournamentCreatedEvent(
                saved.getId(),
                saved.getName(),
                saved.getGame(),
                saved.getLocation(),
                saved.getStartTime(),
                saved.getMaxPlayers()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_TOURNAMENT_CREATED,
                event
        );

        TournamentResponse response = convertTournamentToTournamentResponse(tournament);
        return ResponseEntity.created(URI.create("/api/tournaments/" + tournament.getId()))
                .body(EntityModel.of(response));
    }

    @Override
    public ResponseEntity<EntityModel<TournamentResponse>> updateTournament(UUID id, TournamentUpdateRequest request) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", id));

        tournament.setName(request.name());
        tournament.setGame(request.game());
        tournament.setStartTime(request.startTime());
        tournament.setMaxPlayers(request.maxPlayers());
        tournament.setDescription(request.description());
        tournament.setLocation(request.location());
        tournamentRepository.update(tournament);

        TournamentUpdatedEvent event = new TournamentUpdatedEvent(
                tournament.getId(),
                tournament.getName(),
                tournament.getGame(),
                tournament.getLocation(),
                tournament.getStartTime(),
                tournament.getMaxPlayers()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_TOURNAMENT_UPDATED,
                event
        );

        TournamentResponse response = convertTournamentToTournamentResponse(tournament);
        return ResponseEntity.ok(EntityModel.of(response));
    }

    @Override
    public EntityModel<TournamentResponse> getTournamentById(UUID id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", id));
        return EntityModel.of(convertTournamentToTournamentResponse(tournament));
    }

    @Override
    public List<TournamentResponse> getAllTournaments() {
        return tournamentRepository.findAll(0, Integer.MAX_VALUE)
                .stream()
                .map(this::convertTournamentToTournamentResponse)
                .toList();
    }


    @Override
    public StatusResponse deleteTournament(UUID id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", id));

        tournament.setStatus(TournamentStatus.DELETED);
        tournamentRepository.save(tournament);
        TournamentDeletedEvent event = new TournamentDeletedEvent(id);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_TOURNAMENT_DELETED,
                event
        );

        return new StatusResponse("success", null);
    }

    @Override
    public PagedModel<EntityModel<TournamentResponse>> listTournaments(int page, int size) {
        List<Tournament> tournaments = tournamentRepository.findAll(page - 1, size);
        long totalElements = tournamentRepository.countAll();
        List<EntityModel<TournamentResponse>> models = tournaments.stream()
                .map(t -> tournamentModelAssembler.toModel(convertTournamentToTournamentResponse(t)))
                .collect(Collectors.toList());
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, totalElements);
        return PagedModel.of(models, metadata);
    }

    @Override
    public StatusResponse draw(UUID tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", tournamentId));
        List<Participation> participants = participationRepository.findByTournamentId(tournamentId);
        if (participants.isEmpty()) {
            throw new RuntimeException("Нет участников, записанных на этот турнир");
        }

        if (tournament.getStatus() != TournamentStatus.ACTIVE &&
                tournament.getStatus() != TournamentStatus.FINISHED) {
            throw new IllegalStateException("Жеребьёвка возможна только для ACTIVE или REGISTRATION_CLOSED");
        }
        Collections.shuffle(participants);
        for (int i = 0; i < participants.size(); i++) {
            Participation p = participants.get(i);
            p.setStatus(ParticipationStatus.DRAWN);
            tournament.setStatus(TournamentStatus.DRAWN);
            p.setPosition(i + 1);
            participationRepository.save(p);
        }

        TournamentDrawPerformedEvent drawEvent = new TournamentDrawPerformedEvent(
                tournamentId,
                tournament.getName()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_TOURNAMENT_DRAW,
                drawEvent
        );

        return new StatusResponse("success", null);
    }

    @Override
    public CollectionModel<EntityModel<DrawResultResponse>> getDrawResults(UUID tournamentId) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", tournamentId));
        List<DrawResultResponse> results = participationRepository.findByTournamentId(tournamentId)
                .stream()
                .filter(p -> p.getStatus() == ParticipationStatus.DRAWN)
                .map(p -> new DrawResultResponse(p.getUser().getId(), p.getPosition()))
                .sorted(Comparator.comparingInt(DrawResultResponse::getPosition))
                .collect(Collectors.toList());
        return CollectionModel.of(results.stream().map(EntityModel::of).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<EntityModel<TournamentResultResponse>> postTournamentResults(UUID tournamentId, TournamentResultRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", tournamentId));

        List<Participation> participations = participationRepository.findByTournamentId(tournamentId);
        for (Map.Entry<UUID, Integer> entry : request.results().entrySet()) {
            UUID userId = entry.getKey();
            Integer place = entry.getValue();
            Participation participation = participations.stream()
                    .filter(p -> p.getUser().getId().equals(userId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь", request.results().get(userId)));
            participation.setPlace(place);
            participation.setStatus(ParticipationStatus.FINISHED);
            participationRepository.save(participation);
        }

        tournament.setStatus(TournamentStatus.FINISHED);
        tournamentRepository.save(tournament);

        TournamentResultResponse response = new TournamentResultResponse(tournamentId, request.results());

        TournamentResultsPostedEvent event = new TournamentResultsPostedEvent(
                tournamentId,
                request.results()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_TOURNAMENT_RESULTS,
                event
        );

        return ResponseEntity.created(URI.create("/api/tournaments/" + tournamentId + "/results"))
                .body(resultModelAssembler.toModel(response));
    }

    @Override
    public CollectionModel<EntityModel<UserResponse>> getParticipants(UUID tournamentId) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", tournamentId));
        List<UserResponse> participants = participationRepository.findByTournamentId(tournamentId)
                .stream()
                .map(p -> new UserResponse(p.getUser().getId(), p.getUser().getName()))
                .collect(Collectors.toList());
        return CollectionModel.of(participants.stream().map(EntityModel::of).collect(Collectors.toList()));
    }

    @Override
    public EntityModel<TournamentResultResponse> getTournamentResults(UUID tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Турнир", tournamentId));
        List<Participation> participation = participationRepository.findByTournamentId(tournamentId);
        TournamentResultResponse response = convertTournamentToTournamentResultResponse(tournament, participation);
        return tournamentResultModelAssembler.toModel(response);
    }


    public TournamentResponse convertTournamentToTournamentResponse(Tournament tournament) {
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

    public TournamentResultResponse convertTournamentToTournamentResultResponse(Tournament tournament, List<Participation> participations) {
        TournamentResultResponse response = new TournamentResultResponse((tournament.getId()),
                participations.stream()
                        .collect(Collectors.toMap(p -> p.getUser().getId(), Participation::getPlace)));
        return response;
    }
}
