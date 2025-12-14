package com.example.tournamentsapi.endpoints;

import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.dto.tournaments.DrawResultResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentRequest;
import com.example.tournamentsapi.dto.tournaments.TournamentResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentResultRequest;
import com.example.tournamentsapi.dto.tournaments.TournamentResultResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@Tag(name = "tournaments", description = "API для турниров по настольным играм")
public interface TournamentApi {

    @Operation(summary = "Создать новый турнир")
    @ApiResponse(responseCode = "201", description = "Турнир создан")
    @PostMapping(value = "/api/tournaments", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<TournamentResponse>> createTournament(@Valid @RequestBody TournamentRequest request);

    @Operation(summary = "Обновить турнир")
    @ApiResponse(responseCode = "200", description = "Турнир обновлён")
    @PutMapping(value = "/api/tournaments/{id}")
    ResponseEntity<EntityModel<TournamentResponse>> updateTournament(@PathVariable UUID id, @Valid @RequestBody TournamentUpdateRequest request);

    @Operation(summary = "Удалить турнир")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Турнир удалён", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Турнир не найден", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StatusResponse.class)))
    })
    @DeleteMapping(value = "/api/tournaments/{id}")
    StatusResponse deleteTournament(@PathVariable UUID id);

    @Operation(summary = "Получить турнир по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Турнир найден"),
            @ApiResponse(responseCode = "404", description = "Турнир не найден", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StatusResponse.class)))
    })
    @GetMapping(value = "/api/tournaments/{id}")
    EntityModel<TournamentResponse> getTournament(@PathVariable UUID id);

    @Operation(summary = "Получить список турниров")
    @ApiResponse(responseCode = "200", description = "Список турниров")
    @GetMapping(value = "/api/tournaments")
    PagedModel<EntityModel<TournamentResponse>> listTournaments(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size
    );

    @Operation(summary = "Получить участников турнира")
    @ApiResponse(responseCode = "200", description = "Список участников")
    @GetMapping(value = "/api/tournaments/{id}/participants")
    CollectionModel<EntityModel<UserResponse>> getParticipants(@PathVariable UUID id);

    @Operation(summary = "Провести жеребьёвку")
    @ApiResponse(responseCode = "200", description = "Жеребьёвка проведена")
    @PostMapping("/api/tournaments/{id}/draw")
    StatusResponse draw(@PathVariable UUID id);

    @Operation(summary = "Результаты жеребьёвки")
    @ApiResponse(responseCode = "200", description = "Результаты жеребьёвки")
    @GetMapping("/api/tournaments/{id}/draw/results")
    CollectionModel<EntityModel<DrawResultResponse>> getDrawResults(@PathVariable UUID id);

    @Operation(summary = "Записать результаты турнира")
    @ApiResponse(responseCode = "201", description = "Результаты записаны")
    @PostMapping(value = "/api/tournaments/{id}/results", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<TournamentResultResponse>> postTournamentResults(@PathVariable UUID id, @Valid @RequestBody TournamentResultRequest request);

    @Operation(summary = "Получить результаты турнира")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результаты турнира найдены"),
            @ApiResponse(responseCode = "404", description = "Турнир не найден", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StatusResponse.class)))
    })
    @GetMapping(value = "/api/tournaments/{id}/results")
    EntityModel<TournamentResultResponse> getTournamentResults(@PathVariable UUID id);
}