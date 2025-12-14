package ru.rutmiit.analytics_service.listeners;

import com.rabbitmq.client.Channel;
import org.example.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnalyticsEventListener {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsEventListener.class);

    private static final String DLX = "dlx-exchange";
    private static final String DLQ_ROUTING_KEY = "dlq.analytics";

    private final Set<UUID> processedCreations = ConcurrentHashMap.newKeySet();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "analytics.tournament-created", durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = DLX),
                            @Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
                    }),
            exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
            key = "tournament.created"
    ))
    public void handleTournamentCreated(@Payload TournamentCreatedEvent event,
                                        Channel channel,
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        UUID id = event.tournamentId();

        if (!processedCreations.add(id)) {
            log.warn("Duplicate TournamentCreatedEvent ignored: {}", id);
            channel.basicAck(deliveryTag, false);
            return;
        }

        try {
            log.info("Tournament created: {} | {} | {}", event.name(), event.game(), id);

//            if (event.name() != null && event.name().equalsIgnoreCase("CRASH")) {
//                throw new RuntimeException("Simulating processing error for DLQ test");
//            }

//            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process TournamentCreatedEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "analytics.tournament-deleted", durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = DLX),
                            @Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
                    }),
            exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
            key = "tournament.deleted"
    ))
    public void handleTournamentDeleted(@Payload TournamentDeletedEvent event,
                                        Channel channel,
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Tournament deleted: {}", event.tournamentId());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process TournamentDeletedEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "analytics.player-registration", durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = DLX),
                            @Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
                    }),
            exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
            key = "player.registered"
    ))
    public void handlePlayerRegistered(@Payload PlayerRegisteredEvent event,
                                       Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Player registered: {} → {}", event.userId(), event.game());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process PlayerRegisteredEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "analytics.tournament-results", durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = DLX),
                            @Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
                    }),
            exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
            key = "tournament.results"
    ))
    public void handleResultsPosted(@Payload TournamentResultsPostedEvent event,
                                    Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Results posted for tournament: {}", event.tournamentId());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process TournamentResultsPostedEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "analytics.player-cancellation", durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = DLX),
                            @Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
                    }),
            exchange = @Exchange(name = "tournaments-exchange", type = "topic", durable = "true"),
            key = "player.cancelled"
    ))
    public void handlePlayerCancelled(@Payload PlayerCancelledEvent event,
                                      Channel channel,
                                      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Player cancelled: {} from tournament {}", event.userId(), event.tournamentId());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process PlayerCancelledEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dlq.analytics", durable = "true"),
            exchange = @Exchange(name = "dlx-exchange", type = "topic", durable = "true"),
            key = "dlq.analytics"
    ))
    public void handleDlqMessages(Object failedMessage) {
        log.error("!!! MESSAGE IN DLQ: {}", failedMessage);
    }

//    @RabbitListener(
//            bindings = @QueueBinding(
//                    value = @Queue(name = "q.analytics.schedule-check", durable = "true"),
//                    exchange = @Exchange(name = "schedule-check-fanout", type = "fanout")
//            )
//    )
//    public void handleScheduleCheck(@Payload RegistrationCheckedEvent event) {
//        log.info("ANALYTICS: Registration check for user {} on tournament {} | Available: {} | Reason: {}",
//                event.userId(),
//                event.tournamentId(),
//                event.available(),
//                event.reason());
//    }
}