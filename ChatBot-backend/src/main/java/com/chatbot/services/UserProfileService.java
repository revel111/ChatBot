package com.chatbot.services;

import com.chatbot.controllers.dto.request.ChangePasswordDto;
import com.chatbot.controllers.dto.request.SignInRequestDto;
import com.chatbot.controllers.dto.request.SignUpRequestDto;
import com.chatbot.controllers.dto.request.UpdateUserProfileDto;
import com.chatbot.controllers.dto.response.AuthDataResponse;
import com.chatbot.controllers.dto.response.UserProfileDto;
import com.chatbot.entities.UserProfile;
import com.chatbot.entities.UserProfileToken;
import com.chatbot.exceptions.InvalidRefreshTokenException;
import com.chatbot.exceptions.PasswordsNotMatchException;
import com.chatbot.exceptions.UnauthorizedException;
import com.chatbot.exceptions.UserNotFoundException;
import com.chatbot.exceptions.UserProfileExistsException;
import com.chatbot.mappers.EntityMapper;
import com.chatbot.repositories.UserProfileRepository;
import com.chatbot.repositories.UserProfileTokenRepository;
import com.chatbot.security.JwtCore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new UserProfileExistsException();
        });

        if (!signUpRequestDto.password().equals(signUpRequestDto.confirmPassword()))
            throw new PasswordsNotMatchException();

        var userProfile = entityMapper.toUserProfile(signUpRequestDto);
        userProfile.setPassword(passwordEncoder.encode(signUpRequestDto.password()));
        userProfileRepository.save(userProfile);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signUpRequestDto.email(), signUpRequestDto.password()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException();
        }

//        SecurityContextHolder.getContext().setAuthentication(authentication);
        var authDataResponse = jwtCore.generateTokens(authentication);
        userProfileTokenRepository.save(new UserProfileToken(userProfile.getId(), authDataResponse.jwtTokens().refreshToken()));
        return authDataResponse;
    }

    @Transactional
    public AuthDataResponse signIn(SignInRequestDto signInRequestDto) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDto.email(), signInRequestDto.password()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException();
        }
        var userProfile = userProfileRepository.findByEmail(signInRequestDto.email()).orElseThrow(UserNotFoundException::new);
        var authDataResponse = jwtCore.generateTokens(authentication);

        userProfileTokenRepository.save(new UserProfileToken(userProfile.getId(), authDataResponse.jwtTokens().refreshToken()));
        return authDataResponse;
    }

    @Transactional
    public void logOut(String refreshToken) {
        if (refreshToken != null && !refreshToken.startsWith("Bearer "))
            userProfileTokenRepository.deleteByToken(refreshToken.substring(7));
        else
            throw new InvalidRefreshTokenException();
    }

    @Transactional
    public AuthDataResponse refresh(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer "))
            throw new InvalidRefreshTokenException();

        String token = refreshToken.substring(7);
        var userProfileToken = userProfileTokenRepository.findByToken(token).orElseThrow(UnauthorizedException::new);
        var userProfile = getById(userProfileToken.getId());
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userProfile.getEmail(), userProfile.getPassword()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException();
        }

        var authDataResponse = jwtCore.generateTokens(authentication);

        userProfileTokenRepository.save(new UserProfileToken(userProfile.getId(), authDataResponse.jwtTokens().refreshToken()));
        return authDataResponse;
    }

    public UserProfileDto getInfo(UUID uuid) {
        return entityMapper.toUserProfileDto(getById(uuid));
    }

    public UserProfile getById(UUID userId) {
        return userProfileRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void deleteById(UUID userId) {
        userProfileRepository.deleteById(userId);
        userProfileTokenRepository.deleteById(userId);
    }

    // ? send new tokens & info
    @Transactional
    public UserProfileDto updateProfile(UpdateUserProfileDto signUpRequestDto, UUID userId) {
        var userProfile = getById(userId);

        if (!userProfile.getEmail().equals(signUpRequestDto.email()))
            userProfileRepository.findByEmail(signUpRequestDto.email()).ifPresent(user -> {
                throw new UserProfileExistsException();
            });

        userProfile.setEmail(signUpRequestDto.email());
        userProfile.setUsername(signUpRequestDto.username());

        return entityMapper.toUserProfileDto(userProfileRepository.save(userProfile));
    }

    // ? send new tokens & info
    @Transactional
    public void updatePassword(ChangePasswordDto changePasswordDto, UUID userId) {
        var userProfile = getById(userId);

        if (!passwordEncoder.matches(changePasswordDto.oldPassword(), userProfile.getPassword()))
            throw new UnauthorizedException();

        if (!changePasswordDto.newPassword().equals(changePasswordDto.confirmPassword()))
            throw new PasswordsNotMatchException();

        userProfile.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
        userProfileRepository.save(userProfile);
    }
}
