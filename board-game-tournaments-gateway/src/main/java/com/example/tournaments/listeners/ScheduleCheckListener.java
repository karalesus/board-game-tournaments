package com.example.tournaments.listeners;

import org.example.RegistrationCheckedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCheckListener {

    private static final Logger log = LoggerFactory.getLogger(ScheduleCheckListener.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    // Уникальное имя очереди для gateway
                    value = @Queue(name = "q.gateway.schedule-check.log", durable = "true"),
                    exchange = @Exchange(name = "schedule-check-fanout", type = "fanout")
            )
    )
    public void logScheduleCheck(@Payload RegistrationCheckedEvent event) {
        log.info("Schedule check completed: User {} → Tournament {} | Available: {} | Reason: {}",
                event.userId(),
                event.tournamentId(),
                event.available(),
                event.reason());
    }
}