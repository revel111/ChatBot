package com.chatbot.repositories;

import com.chatbot.entities.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("SELECT c FROM Chat c WHERE c.userProfile.id = :userId")
    Page<Chat> findAllByUserId(UUID userId, Pageable pageable);

    Boolean existsByIdAndUserProfileId(UUID id, UUID userProfile_id);

}
