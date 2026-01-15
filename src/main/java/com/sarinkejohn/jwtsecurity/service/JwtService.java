package com.sarinkejohn.jwtsecurity.service;


import com.sarinkejohn.jwtsecurity.dto.TokenPair;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.security.Key;


@Slf4j
@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;
    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpirationInMs;



    //gen access token
    public String generateAccessToken(Authentication authentication) {
        return generateAccessToken(authentication, jwtExpirationMs, new HashMap<>());
    }

    //generate refresh token
    public String generateRefreshToken(Authentication authentication) {

        Map<String, String> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        return generateAccessToken(authentication, refreshExpirationInMs, claims);

    }
    //validate token
    public boolean validateTokenForUser(String token,UserDetails userDetails) {
        final String username = extractUsernameFromToken(token) ;//Extract username from token
        return username != null
                && username.equals(userDetails.getUsername());

    }
    public boolean isValidToken(String token) {
        return extractAllClaims(token) != null;
    }
    public   String extractUsernameFromToken(String token) {
        Claims claims =  extractAllClaims(token);
        if(claims != null) {
            return claims.getSubject();
        }
        return null;
    }


    //validate if the refresh token is refresh token
    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        if(claims == null) {
            return false;
        }
        return "refresh".equals(claims.get("tokenType"));
    }

    private Claims extractAllClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSignInkey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return claims;
    }

    private String generateAccessToken(Authentication authentication, long expirationMs, Map<String, String> claims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();//time for the token creation
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .header()
                .add("typ","JWT")
                .and()
                .subject(userPrincipal.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith((Key) getSigInkey())
                .claims(claims)
                .compact();
    }

    private Object getSigInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private SecretKey getSignInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenPair generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenPair(accessToken,refreshToken);
    }
}
