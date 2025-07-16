package com.chatbot.services;

import com.chatbot.controllers.dto.request.ChangePasswordDto;
import com.chatbot.controllers.dto.request.SignInRequestDto;
import com.chatbot.controllers.dto.request.SignUpRequestDto;
import com.chatbot.controllers.dto.request.UpdateUserProfileDto;
import com.chatbot.controllers.dto.response.AuthDataResponse;
import com.chatbot.controllers.dto.response.UserProfileDto;
import com.chatbot.entities.UserProfile;
import com.chatbot.entities.UserProfileToken;
import com.chatbot.mappers.EntityMapper;
import com.chatbot.repositories.UserProfileRepository;
import com.chatbot.repositories.UserProfileTokenRepository;
import com.chatbot.security.JwtCore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtCore jwtCore;

    private final EntityMapper entityMapper;

    private final UserProfileTokenRepository userProfileTokenRepository;

    @Transactional
    public AuthDataResponse signUp(SignUpRequestDto signUpRequestDto) {
        userProfileRepository.findByEmail(signUpRequestDto.email()).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists.");
        });

        if (!signUpRequestDto.password().equals(signUpRequestDto.confirmPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match.");

        var userProfile = entityMapper.toUserProfile(signUpRequestDto);
        userProfile.setPassword(passwordEncoder.encode(signUpRequestDto.password()));
        userProfileRepository.save(userProfile);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signUpRequestDto.email(), signUpRequestDto.password()));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized operation");
        }

        var authDataResponse = jwtCore.generateTokens(authentication);
        userProfileTokenRepository.save(new UserProfileToken(userProfile.getId(), authDataResponse.jwtTokens().refreshToken(), authDataResponse.jwtTokens().refreshExpirationTime()));
        return authDataResponse;
    }

    @Transactional
    public AuthDataResponse signIn(SignInRequestDto signInRequestDto) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDto.email(), signInRequestDto.password()));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized operation");
        }

        var userProfile = userProfileRepository.findByEmail(signInRequestDto.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var authDataResponse = jwtCore.generateTokens(authentication);

        userProfileTokenRepository.save(new UserProfileToken(userProfile.getId(), authDataResponse.jwtTokens().refreshToken(), authDataResponse.jwtTokens().refreshExpirationTime()));
        return authDataResponse;
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.startsWith("Bearer "))
            userProfileTokenRepository.deleteByToken(refreshToken.substring(7));
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token");
    }

    @Transactional
    public AuthDataResponse refresh(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token");

        String token = refreshToken.substring(7);
        var userProfileToken = userProfileTokenRepository.findByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized operation"));
        var userProfile = getById(userProfileToken.getId());
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userProfile.getEmail(), userProfile.getPassword()));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized operation");
        }

        var authDataResponse = jwtCore.generateTokens(authentication);

        userProfileTokenRepository.save(new UserProfileToken(userProfile.getId(), authDataResponse.jwtTokens().refreshToken(), authDataResponse.jwtTokens().refreshExpirationTime()));
        return authDataResponse;
    }

    public UserProfileDto getInfo(UUID uuid) {
        return entityMapper.toUserProfileDto(getById(uuid));
    }

    public UserProfile getById(UUID userId) {
        return userProfileRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public void deleteById(UUID userId) {
        userProfileRepository.deleteById(userId);
        userProfileTokenRepository.deleteById(userId);
    }

    // ? send new tokens & info
    @Transactional
    public UserProfileDto updateProfile(UpdateUserProfileDto updateUserProfileDto, UUID userId) {
        var userProfile = getById(userId);

        if (!userProfile.getEmail().equals(updateUserProfileDto.email()))
            userProfileRepository.findByEmail(updateUserProfileDto.email()).ifPresent(user -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists.");
            });

        userProfile.setEmail(updateUserProfileDto.email());
        userProfile.setUsername(updateUserProfileDto.username());

        return entityMapper.toUserProfileDto(userProfileRepository.save(userProfile));
    }

    // ? send new tokens & info
    @Transactional
    public void updatePassword(ChangePasswordDto changePasswordDto, UUID userId) {
        var userProfile = getById(userId);

        if (!passwordEncoder.matches(changePasswordDto.oldPassword(), userProfile.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized operation");

        if (!changePasswordDto.newPassword().equals(changePasswordDto.confirmPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match.");

        userProfile.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
        userProfileRepository.save(userProfile);
    }


    @Scheduled(fixedDelay = 1000 * 60 * 60) // every hour
    @Transactional
    public void deleteExpiredTokens() {
        userProfileTokenRepository.deleteAllByExpirationDateBefore(Instant.now());
    }

}
