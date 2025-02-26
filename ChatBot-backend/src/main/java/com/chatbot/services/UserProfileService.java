package com.chatbot.services;

import com.chatbot.controllers.dto.request.SignInRequestDto;
import com.chatbot.controllers.dto.request.SignUpRequestDto;
import com.chatbot.controllers.dto.response.JwtTokenResponse;
import com.chatbot.controllers.dto.response.UserProfileDto;
import com.chatbot.entities.UserProfile;
import com.chatbot.exceptions.UnauthorizedException;
import com.chatbot.exceptions.UserNotFoundException;
import com.chatbot.exceptions.UserProfileExistsException;
import com.chatbot.mappers.EntityMapper;
import com.chatbot.repositories.UserProfileRepository;
import com.chatbot.security.JwtCore;
import com.chatbot.security.contexts.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        userProfileRepository.findUserProfileByEmail(signUpRequestDto.email()).ifPresent(user -> {
            throw new UserProfileExistsException();
        });
        var userProfile = entityMapper.toUserProfile(signUpRequestDto);
        userProfile.setPassword(passwordEncoder.encode(signUpRequestDto.password()));

        userProfileRepository.save(userProfile);
    }

    public JwtTokenResponse signIn(SignInRequestDto signInRequestDto) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDto.email(), signInRequestDto.password()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new JwtTokenResponse(jwtCore.generateToken(authentication));
    }

    public UserProfileDto getInfo(AuthContext authContext) {
        return entityMapper.toUserProfileDto(authContext);
    }

    public UserProfile getById(UUID userId) {
        return userProfileRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
