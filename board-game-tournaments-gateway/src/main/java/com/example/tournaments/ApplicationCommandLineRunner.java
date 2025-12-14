package com.example.tournaments;

import com.example.tournaments.models.Tournament;
import com.example.tournaments.models.User;
import com.example.tournaments.models.enums.Role;
import com.example.tournaments.repositories.impl.TournamentRepositoryImpl;
import com.example.tournaments.repositories.impl.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    private final UserRepositoryImpl userRepository;
    private final TournamentRepositoryImpl tournamentRepository;
    private final String defaultPassword;

    @Autowired
    public ApplicationCommandLineRunner(UserRepositoryImpl userRepository,
                                        TournamentRepositoryImpl tournamentRepository,
                                        @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
        this.defaultPassword = defaultPassword;
    }

    @Override
    public void run(String... args) {
        initAdmin();
        initUsers();
        initTournaments();
    }

    private void initAdmin() {
        if (userRepository.existsByEmail("admin@mail.ru")) {
            System.out.println("Администратор уже существует.");
            return;
        }

        User adminUser = new User(
                "Admin",
                "admin@mail.ru",
                defaultPassword
        );
        adminUser.setRoles(List.of(Role.ADMIN));

        userRepository.save(adminUser);

        System.out.println("Администратор создан с email: admin@mail.ru");
    }

    private void initUsers() {
        if (!userRepository.existsByEmail("user1@mail.ru")) {
            User user1 = new User("Иван", "user1@mail.ru", defaultPassword);
            user1.setRoles(List.of(Role.USER));
            userRepository.save(user1);
        }

        if (!userRepository.existsByEmail("user2@mail.ru")) {
            User user2 = new User("Мария", "user2@mail.ru", defaultPassword);
            user2.setRoles(List.of(Role.USER));
            userRepository.save(user2);
        }

        if (!userRepository.existsByEmail("user3@mail.ru")) {
            User user3 = new User("Павел", "user3@mail.ru", defaultPassword);
            user3.setRoles(List.of(Role.USER));
            userRepository.save(user3);
        }

        if (!userRepository.existsByEmail("user4@mail.ru")) {
            User user4 = new User("Митрофан", "user4@mail.ru", defaultPassword);
            user4.setRoles(List.of(Role.USER));
            userRepository.save(user4);
        }

        if (!userRepository.existsByEmail("user5@mail.ru")) {
            User user5 = new User("Алексей", "user5@mail.ru", defaultPassword);
            user5.setRoles(List.of(Role.USER));
            userRepository.save(user5);
        }

        if (!userRepository.existsByEmail("user6@mail.ru")) {
            User user6 = new User("Дарья", "user6@mail.ru", defaultPassword);
            user6.setRoles(List.of(Role.USER));
            userRepository.save(user6);
        }

        System.out.println("Тестовые пользователи инициализированы.");
    }

    private void initTournaments() {
        if (tournamentRepository.findAll().isEmpty()) {
            Tournament t1 = new Tournament(
                    "Чемпионат по шахматам",
                    "Шахматы",
                    LocalDateTime.now().plusDays(7),
                    8,
                    "Турнир для любителей шахмат.",
                    "Клуб настольных игр, Москва",
                    LocalDateTime.now(),
                    0,
                    com.example.tournamentsapi.enums.TournamentStatus.ACTIVE
            );

            Tournament t2 = new Tournament(
                    "Монополия Battle",
                    "Монополия",
                    LocalDateTime.now().plusDays(14),
                    6,
                    "Интенсивная игра в классическую Монополию.",
                    "Коворкинг, Санкт-Петербург",
                    LocalDateTime.now(),
                    0,
                    com.example.tournamentsapi.enums.TournamentStatus.ACTIVE
            );

            Tournament t3 = new Tournament(
                    "Каркассон Турнир",
                    "Каркассон",
                    LocalDateTime.now().plusDays(21),
                    12,
                    "Стратегическая настолка для всех желающих.",
                    "Антикафе, Казань",
                    LocalDateTime.now(),
                    0,
                    com.example.tournamentsapi.enums.TournamentStatus.ACTIVE
            );

            tournamentRepository.save(t1);
            tournamentRepository.save(t2);
            tournamentRepository.save(t3);

            System.out.println("Тестовые турниры по настольным играм инициализированы.");
        }
    }
}

