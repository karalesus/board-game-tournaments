package com.example.tournamentsapi.endpoints;

import com.example.tournamentsapi.dto.auth.UserResponse;
import com.example.tournamentsapi.dto.participations.ParticipationResponse;
import com.example.tournamentsapi.dto.participations.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "profile", description = "API для получения профиля пользователя")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StatusResponse.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StatusResponse.class))),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StatusResponse.class)))
})
public interface UserProfileApi {

    @Operation(summary = "Получить профиль пользователя")
    @GetMapping(value = "/api/profile/{userId}")
    EntityModel<UserResponse> getUserProfile(@PathVariable UUID userId);

    @Operation(summary = "Отменить регистрацию на турнир")
    @DeleteMapping(value = "/api/profile/{userId}/registrations/{tournamentId}")
    StatusResponse cancelTournamentRegistration(@PathVariable UUID userId, @PathVariable UUID tournamentId);

    @Operation(summary = "Получить список турниров пользователя")
    @GetMapping("/api/profile/{userId}/registrations")
    CollectionModel<EntityModel<ParticipationResponse>> listUserTournaments(@PathVariable UUID userId);

    @Operation(summary = "Получить список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    @GetMapping(value = "/api/profile/allUsers")
    CollectionModel<EntityModel<UserResponse>> getAllUsers();
}