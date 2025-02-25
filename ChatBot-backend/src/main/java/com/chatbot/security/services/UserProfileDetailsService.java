package com.chatbot.security.services;

import com.chatbot.repositories.UserProfileRepository;
import com.chatbot.security.UserProfileDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileDetailsService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userProfile = userProfileRepository.findUserProfileByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found.", email)));

        return UserProfileDetails.builder()
                .id(userProfile.getId())
                .username(userProfile.getUsername())
                .email(userProfile.getEmail())
                .password(userProfile.getPassword())
                .build();
    }
}
