package com.chatbot.security;

import com.chatbot.controllers.dto.response.AuthDataResponse;
import com.chatbot.controllers.dto.response.JwtTokens;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtCore {

    @Value("${app.security.access_secret}")
    private String accessSecret;

    @Value("${app.security.refresh_secret}")
    private String refreshSecret;

    @Value("${app.security.access_expiration}")
    private long accessTokenLifetimeInSeconds;

    @Value("${app.security.refresh_expiration}")
    private long refreshTokenLifetimeInSeconds;

    public AuthDataResponse generateTokens(Authentication authentication) {
        var userProfileDetails = (UserProfileDetails) authentication.getPrincipal();

        var instantNow = Instant.now();
        var accessExpirationTime = instantNow.plusSeconds(accessTokenLifetimeInSeconds);
        var refreshExpirationTime = instantNow.plusSeconds(refreshTokenLifetimeInSeconds);

        return new AuthDataResponse(
                new JwtTokens(
                        generateToken(accessSecret, userProfileDetails, accessExpirationTime),
                        generateToken(refreshSecret, userProfileDetails, refreshExpirationTime),
                        accessExpirationTime
                ),
                userProfileDetails.getId(),
                userProfileDetails.getRealUsername(),
                userProfileDetails.getEmail()
        );
    }

    public String generateToken(String secret, UserProfileDetails userProfileDetails, Instant expirationTime) {
        return Jwts.builder()
                .subject(userProfileDetails.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expirationTime))
                .claim("id", userProfileDetails.getId())
                .claim("username", userProfileDetails.getUsername())
                .claim("email", userProfileDetails.getEmail())
                .signWith(generateKey(secret))
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(generateKey(accessSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey generateKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
