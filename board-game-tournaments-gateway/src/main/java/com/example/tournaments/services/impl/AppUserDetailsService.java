//package com.example.tournaments.services.impl;
//
//import com.example.tournaments.repositories.impl.UserRepositoryImpl;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import java.util.stream.Collectors;
//
//public class AppUserDetailsService implements UserDetailsService {
//    private UserRepositoryImpl userRepository;
//
//    public AppUserDetailsService(UserRepositoryImpl userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userRepository.findByEmail(email)
//                .map(u -> new User(
//                        u.getEmail(),
//                        u.getPassword(),
//                        u.getRoles().stream()
//                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
//                                .collect(Collectors.toList())
//                )).orElseThrow(() -> new UsernameNotFoundException(email + " was not found!"));
//    }
//}
