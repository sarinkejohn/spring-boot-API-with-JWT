package com.sarinkejohn.jwtsecurity.controller;

import com.sarinkejohn.jwtsecurity.dto.LoginRequest;
import com.sarinkejohn.jwtsecurity.dto.RefreshTokenRequest;
import com.sarinkejohn.jwtsecurity.dto.RegisterRequest;
import com.sarinkejohn.jwtsecurity.dto.TokenPair;
import com.sarinkejohn.jwtsecurity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest requset){
        //save a new user to db and return success
        authService.registerUser(requset);
        return ResponseEntity.ok("successful registered user");

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest ){
        TokenPair tokenPair = authService.login(loginRequest);
        return ResponseEntity.ok(tokenPair);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        TokenPair tokenPair = authService.refreshToken(request);
        return ResponseEntity.ok(tokenPair);

    }
}
