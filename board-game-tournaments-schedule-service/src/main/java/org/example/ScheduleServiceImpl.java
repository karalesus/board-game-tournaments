package org.example;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@GrpcService
public class ScheduleServiceImpl extends ScheduleServiceGrpc.ScheduleServiceImplBase {

    private static final int MIN_BUFFER_MINUTES = 30;

    @Override
    public void checkRegistrationAvailability(
            RegistrationAvailabilityRequest request,
            StreamObserver<RegistrationAvailabilityResponse> responseObserver) {

        boolean canRegister = true;
        String reason = "Можно записаться";

        if (request.getCurrentRegistered() >= request.getMaxPlayers()) {
            canRegister = false;
            reason = "Места на турнире закончились";
            sendResponse(responseObserver, canRegister, reason);
            return;
        }

        LocalDateTime newTournamentStart = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(request.getStartTime()),
                ZoneId.systemDefault()
        );
        LocalDateTime newTournamentEnd = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(request.getEndTime()),
                ZoneId.systemDefault()
        );

        for (UserTournament existing : request.getExistingTournamentsList()) {
            LocalDateTime existingStart = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(existing.getStartTime()),
                    ZoneId.systemDefault()
            );
            LocalDateTime existingEnd = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(existing.getEndTime()),
                    ZoneId.systemDefault()
            );

            if (hasTimeOverlap(newTournamentStart, newTournamentEnd, existingStart, existingEnd)) {
                canRegister = false;
                reason = "У вас уже есть турнир в это время";
                sendResponse(responseObserver, canRegister, reason);
                return;
            }

            if (hasInsufficientBuffer(newTournamentStart, newTournamentEnd,
                    existingStart, existingEnd)) {
                canRegister = false;
                reason = String.format(
                        "Между турнирами должен быть интервал минимум %d минут",
                        MIN_BUFFER_MINUTES
                );
                sendResponse(responseObserver, canRegister, reason);
                return;
            }
        }
        sendResponse(responseObserver, canRegister, reason);
    }

    private boolean hasTimeOverlap(
            LocalDateTime start1, LocalDateTime end1,
            LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private boolean hasInsufficientBuffer(
            LocalDateTime newStart, LocalDateTime newEnd,
            LocalDateTime existingStart, LocalDateTime existingEnd) {

        if (newStart.isAfter(existingEnd)) {
            long minutesBetween = java.time.Duration.between(existingEnd, newStart).toMinutes();
            return minutesBetween < MIN_BUFFER_MINUTES;
        }

        if (newEnd.isBefore(existingStart)) {
            long minutesBetween = java.time.Duration.between(newEnd, existingStart).toMinutes();
            return minutesBetween < MIN_BUFFER_MINUTES;
        }

        return false;
    }


    private void sendResponse(StreamObserver<RegistrationAvailabilityResponse> responseObserver, boolean available, String reason) {
        RegistrationAvailabilityResponse response =
                RegistrationAvailabilityResponse.newBuilder()
                        .setAvailable(available)
                        .setReason(reason)
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
