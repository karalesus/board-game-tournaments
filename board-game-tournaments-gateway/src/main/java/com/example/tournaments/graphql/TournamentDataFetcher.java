package com.example.tournaments.graphql;

import com.example.tournaments.services.TournamentService;
import com.example.tournamentsapi.dto.PagedResponse;
import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.dto.tournaments.*;
import com.example.tournamentsapi.enums.TournamentStatus;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
public class TournamentDataFetcher {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentDataFetcher(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @DgsQuery
    public PagedResponse<TournamentResponse> tournaments(@InputArgument Integer page, @InputArgument Integer size) {
        PagedModel<EntityModel<TournamentResponse>> pagedModel = tournamentService.listTournaments(
                page != null ? page : 1,
                size != null ? size : 20
        );
        List<TournamentResponse> content = pagedModel.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());
        PagedModel.PageMetadata metadata = pagedModel.getMetadata();
        return new PagedResponse<>(
                content,
                (int) metadata.getNumber(),
                (int) metadata.getSize(),
                (int) metadata.getTotalElements(),
                (int) metadata.getTotalPages(),
                metadata.getNumber() >= metadata.getTotalPages()
        );
    }

    @DgsQuery
    public TournamentResponse tournamentById(@InputArgument String id) {
        return tournamentService.getTournamentById(UUID.fromString(id)).getContent();
    }

    @DgsQuery
    public List<UserResponse> participants(@InputArgument String tournamentId) {
        CollectionModel<EntityModel<UserResponse>> participants = tournamentService.getParticipants(UUID.fromString(tournamentId));
        return participants.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());
    }

    @DgsQuery
    public List<DrawResultResponse> drawResults(@InputArgument String tournamentId) {
        CollectionModel<EntityModel<DrawResultResponse>> results = tournamentService.getDrawResults(UUID.fromString(tournamentId));
        return results.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());
    }

    @DgsMutation
    public TournamentResponse createTournament(@InputArgument("input") Map<String, Object> input) {
        TournamentRequest request = new TournamentRequest(
                (String) input.get("name"),
                (String) input.get("game"),
                LocalDateTime.parse((String) input.get("startTime")),
                (Integer) input.get("maxPlayers"),
                (String) input.get("description"),
                (String) input.get("location"),
                (TournamentStatus) input.get("status")
        );
        return tournamentService.createTournament(request).getBody().getContent();
    }

    @DgsMutation
    public TournamentResponse updateTournament(@InputArgument String id, @InputArgument("input") Map<String, Object> input) {
        TournamentUpdateRequest request = new TournamentUpdateRequest(
                (String) input.get("name"),
                (String) input.get("game"),
                LocalDateTime.parse((String) input.get("startTime")),
                (Integer) input.get("maxPlayers"),
                (String) input.get("description"),
                (String) input.get("location"),
                (TournamentStatus) input.get("status")
        );
        return tournamentService.updateTournament(UUID.fromString(id), request).getBody().getContent();
    }

    @DgsMutation
    public StatusResponse deleteTournament(@InputArgument String id) {
        return tournamentService.deleteTournament(UUID.fromString(id));
    }

    @DgsMutation
    public StatusResponse draw(@InputArgument String tournamentId) {
        return tournamentService.draw(UUID.fromString(tournamentId));
    }

    @DgsMutation
    public TournamentResultResponse postTournamentResults(@InputArgument String tournamentId, @InputArgument("input") Map<String, Object> input) {
        List<Map<String, Object>> results = (List<Map<String, Object>>) input.get("results");
        Map<UUID, Integer> resultMap = results.stream()
                .collect(Collectors.toMap(
                        entry -> UUID.fromString((String) entry.get("userId")),
                        entry -> (Integer) entry.get("place")
                ));
        TournamentResultRequest request = new TournamentResultRequest(resultMap);
        return tournamentService.postTournamentResults(UUID.fromString(tournamentId), request).getBody().getContent();
    }
}