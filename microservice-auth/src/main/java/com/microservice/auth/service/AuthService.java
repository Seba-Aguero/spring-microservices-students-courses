package com.microservice.auth.service;

import com.microservice.auth.exception.BadRequestException;
import com.microservice.auth.http.request.LoginRequest;
import com.microservice.auth.http.request.SignupRequest;
import com.microservice.auth.http.response.JwtResponse;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.IUserRepository;
import com.microservice.auth.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles);
    }

    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> roles = assignRoles(signupRequest.getRoles());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    private Set<String> assignRoles(Set<String> requestRoles) {
        Set<String> roles = new HashSet<>();

        if (requestRoles == null || requestRoles.isEmpty()) {
            roles.add("USER");
            return roles;
        }

        Set<String> validRoles = getValidRoles();

        for (String role : requestRoles) {
            String upperRole = role.toUpperCase();
            if (!validRoles.contains(upperRole)) {
                throw new BadRequestException("Role not found: " + role);
            }
            roles.add(upperRole);
        }

        return roles;
    }

    private Set<String> getValidRoles() {
        Set<String> validRoles = new HashSet<>();
        validRoles.add("USER");
        validRoles.add("ADMIN");
        validRoles.add("MODERATOR");
        return validRoles;
    }
}


