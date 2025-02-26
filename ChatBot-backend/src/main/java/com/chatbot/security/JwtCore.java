package com.chatbot.security;

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

    @Value("${app.security.secret}")
    private String secret;

    @Value("${app.security.expiration}")
    private long lifetimeInSeconds;

    public String generateToken(Authentication authentication) {
        var userProfileDetails = (UserProfileDetails) authentication.getPrincipal();
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userProfileDetails.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(lifetimeInSeconds)))
                .signWith(generateKey())
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
