package com.chatbot.repositories;

import com.chatbot.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m from Message m WHERE m.chat.id = :chatId")
    Page<Message> findAllByChatId(UUID chatId, Pageable pageable);

}
