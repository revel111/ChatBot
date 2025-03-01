package com.chatbot.repositories;

import com.chatbot.entities.UserProfileToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileTokenRepository extends JpaRepository<UserProfileToken, UUID> {

    void deleteByToken(String token);

    Optional<UserProfileToken> findByToken(String token);

}
