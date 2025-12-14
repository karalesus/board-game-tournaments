//package com.example.tournaments.controllers;
//
//
//import com.example.tournaments.services.impl.AppUserDetailsService;
//import com.example.tournaments.services.impl.UserServiceImpl;
//import com.example.tournamentsapi.dto.auth.AuthRequest;
//import com.example.tournamentsapi.dto.auth.AuthResponse;
//import com.example.tournamentsapi.dto.auth.UserRequest;
//import com.example.tournamentsapi.dto.auth.UserResponse;
//import com.example.tournamentsapi.endpoints.AuthApi;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AuthController implements AuthApi {
//
//    private final UserServiceImpl userService;
//
//    @Autowired
//    public AuthController(UserServiceImpl userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public UserResponse register(@Valid UserRequest request) {
//        return userService.register(request);
//    }
//
//    @Override
//    public AuthResponse login(@Valid AuthRequest request) {
//        return userService;
//    }
//}
