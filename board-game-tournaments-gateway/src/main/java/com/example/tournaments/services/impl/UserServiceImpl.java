//package com.example.tournaments.services.impl;
//
//import com.example.tournaments.models.User;
//import com.example.tournaments.repositories.impl.UserRepositoryImpl;
//import com.example.tournaments.services.UserService;
//import com.example.tournamentsapi.dto.auth.UserRequest;
//import com.example.tournamentsapi.dto.auth.UserResponse;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    private final UserRepositoryImpl userRepository;
//
//    public UserServiceImpl(UserRepositoryImpl userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public String register(UserRequest request) {
//        if (userRepository.existsByEmail(request.email())) {
//            throw new RuntimeException("Email уже используется");
//        }
//
//        User user = new User(
//                request.username(),
//                request.email(),
//                request.password()
//        );
//
//        userRepository.save(user);
//        return "Пользователь успешно зарегистрирован с id: " + user.getId();
//    }
//
//    @Override
//    public List<UserResponse> getAllUsers() {
//        return userRepository.findAll()
//                .stream()
//                .map(this::mapToResponse)
//                .toList();
//    }
//
//    @Override
//    public Optional<UserResponse> getUserById(UUID uuid) {
//        return userRepository.findById(uuid)
//                .map(this::mapToResponse);
//    }
//
//    @Override
//    public Optional<UserResponse> getUserByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .map(this::mapToResponse);
//    }
//
//    private UserResponse mapToResponse(User user) {
//        return new UserResponse(user.getId(), user.getName(), user.getEmail());
//    }
//}
