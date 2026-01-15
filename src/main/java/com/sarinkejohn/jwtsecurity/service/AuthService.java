package com.sarinkejohn.jwtsecurity.service;

import com.sarinkejohn.jwtsecurity.dto.LoginRequest;
import com.sarinkejohn.jwtsecurity.dto.RefreshTokenRequest;
import com.sarinkejohn.jwtsecurity.dto.RegisterRequest;
import com.sarinkejohn.jwtsecurity.dto.TokenPair;
import com.sarinkejohn.jwtsecurity.model.Role;
import com.sarinkejohn.jwtsecurity.model.User;
import com.sarinkejohn.jwtsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.AuthenticationManager;
@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        //check if the user with the same username already exist
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username is exists!");
        }
        //create new user
        User user = User
                .builder()
                .fullName(registerRequest.getFullName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.valueOf(registerRequest.getRole()))
                .build();
        userRepository.save(user);
    }
    public TokenPair login(LoginRequest loginRequest) {
        //auth user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        //set authentication in secContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //gen token pair

        return jwtService.generateTokenPair(authentication);
    }

    public TokenPair refreshToken(@Valid RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        //check if it a valid refresh token
        if(!jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token!");
        }
        String user = jwtService.extractUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);
        if(userDetails == null) {
            throw new IllegalArgumentException("User not found!");
        }
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        String accessToken = jwtService.generateAccessToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }
}
