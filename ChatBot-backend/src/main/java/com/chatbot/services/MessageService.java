package com.chatbot.services;

import com.chatbot.controllers.dto.response.MessageDto;
import com.chatbot.entities.Message;
import com.chatbot.mappers.EntityMapper;
import com.chatbot.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    private final EntityMapper entityMapper;

    public Page<MessageDto> getMessagesByChatId(UUID chatId, Pageable pageable) {
        return messageRepository.findAllByChatId(chatId, pageable).map(entityMapper::toMessageDto);
    }

    @Transactional
    public MessageDto createMessage(Message message) {
        return entityMapper.toMessageDto(messageRepository.save(message));
    }

}
