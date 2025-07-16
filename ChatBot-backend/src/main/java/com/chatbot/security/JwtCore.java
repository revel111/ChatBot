package com.chatbot.security;

import com.chatbot.controllers.dto.response.AuthDataResponse;
import com.chatbot.controllers.dto.response.JwtTokens;
import com.chatbot.properties.SecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtCore {

    private final SecurityProperties securityProperties;

    public AuthDataResponse generateTokens(Authentication authentication) {
        var userProfileDetails = (UserProfileDetails) authentication.getPrincipal();

        var instantNow = Instant.now();
        var accessExpirationTime = instantNow.plusSeconds(securityProperties.getAccessExpiration());
        var refreshExpirationTime = instantNow.plusSeconds(securityProperties.getRefreshExpiration());

        return new AuthDataResponse(
                new JwtTokens(
                        this.generateToken(securityProperties.getAccessSecret(), userProfileDetails, accessExpirationTime),
                        this.generateToken(securityProperties.getRefreshSecret(), userProfileDetails, refreshExpirationTime),
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
                .signWith(this.generateKey(secret))
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(this.generateKey(securityProperties.getAccessSecret()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey generateKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
