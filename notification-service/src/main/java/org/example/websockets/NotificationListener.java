package org.example.websockets;

import org.example.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final NotificationHandler notificationHandler;

    public NotificationListener(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.player-registered", durable = "true"),
                    exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
                    key = "player.registered"
            )
    )
    public void handlePlayerRegistration(@Payload PlayerRegisteredEvent event) {
        log.info("Received PlayerRegisteredEvent: {}", event);

        String userMessage = String.format(
                "{\"type\": \"PLAYER_REGISTERED\", \"tournamentId\": \"%s\", \"message\": \"Вы успешно зарегистрированы на турнир '%s'. Место проведения: %s\"}",
                event.tournamentId(),
                event.game() != null ? event.game() : "турнир",
                event.location() != null ? event.location() : "неизвестном месте"
        );

        notificationHandler.broadcast(userMessage);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.player-cancelled", durable = "true"),
                    exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
                    key = "player.cancelled"
            )
    )
    public void handlePlayerCancellation(@Payload PlayerCancelledEvent event) {
        log.info("Received PlayerCancelledEvent: {}", event);

        String userMessage = String.format(
                "{\"type\": \"CANCELLED\", \"tournamentId\": \"%s\", \"message\": \"Вы отменили регистрацию на турнир\"}",
                event.tournamentId()
        );

        notificationHandler.broadcast(userMessage);
    }


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.tournament-updated", durable = "true"),
                    exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
                    key = "tournament.updated"
            )
    )
    public void handleTournamentUpdated(@Payload TournamentUpdatedEvent event) {
        log.info("Received TournamentUpdatedEvent: {}", event);

        String userMessage = String.format(
                "{\"type\": \"UPDATED\", \"tournamentId\": \"%s\", \"message\": \"Турнир '%s' обновлён. Проверьте расписание!\"}",
                event.tournamentId(),
                event.name() != null ? event.name() : "без названия"
        );

        notificationHandler.broadcast(userMessage);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.tournament-deleted", durable = "true"),
                    exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
                    key = "tournament.deleted"
            )
    )
    public void handleTournamentDeleted(@Payload TournamentDeletedEvent event) {
        log.info("Received TournamentDeletedEvent: {}", event);

        String userMessage = String.format(
                "{\"type\": \"DELETED\", \"tournamentId\": \"%s\", \"message\": \"Турнир отменён организаторами\"}",
                event.tournamentId()
        );

        notificationHandler.broadcast(userMessage);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.tournament-results", durable = "true"),
                    exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
                    key = "tournament.results"
            )
    )
    public void handleTournamentResults(@Payload TournamentResultsPostedEvent event) {
        log.info("Received TournamentResultsPostedEvent: {}", event);

        String resultsJson = event.results().entrySet().stream()
                .map(entry -> String.format("\"%s\": %d", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", ", "{", "}"));

        String userMessage = String.format(
                "{\"type\": \"TOURNAMENT_RESULTS\", \"tournamentId\": \"%s\", \"results\": %s}",
                event.tournamentId(),
                resultsJson
        );

        notificationHandler.broadcast(userMessage);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.tournament-draw", durable = "true"),
                    exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
                    key = "tournament.draw"
            )
    )
    public void handleTournamentDraw(TournamentDrawPerformedEvent event) {
        log.info("Received TournamentDrawPerformedEvent: {}", event);

        String userMessage = String.format(
                "{\"type\": \"DRAW\", \"tournamentId\": \"%s\", \"tournamentName\": \"%s\", \"message\": \"Жеребьёвка турнира '%s' завершена!\"}",
                event.tournamentId(),
                event.tournamentName() != null ? event.tournamentName() : "без названия",
                event.tournamentName() != null ? event.tournamentName() : "без названия"
        );

        notificationHandler.broadcast(userMessage);
    }
}
