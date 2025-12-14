package com.example.tournamentsapi.endpoints;

import com.example.tournamentsapi.dto.participations.ParticipationRequest;
import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import com.example.tournamentsapi.dto.tournaments.TournamentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "participation", description = "API для регистрации игроков на турниры")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = StatusResponse.class))),
        @ApiResponse(responseCode = "409", description = "Конфликт расписания", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StatusResponse.class))),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
})
public interface ParticipationApi {

    @Operation(summary = "Зарегистрировать игрока на турнир")
    @ApiResponse(responseCode = "201", description = "Регистрация успешна")
    @PostMapping(value = "/api/tournaments/participation/participate", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<ParticipationResponse>> participate(@Valid @RequestBody ParticipationRequest request);

    @Operation(summary = "Список предстоящих турниров")
    @ApiResponse(responseCode = "200", description = "Список предстоящих турниров")
    @GetMapping(value = "/api/tournaments/upcoming")
    CollectionModel<EntityModel<TournamentResponse>> listUpcomingTournaments();
}
