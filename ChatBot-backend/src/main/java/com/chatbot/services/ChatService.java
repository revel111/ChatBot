package com.chatbot.services;

import com.chatbot.controllers.dto.request.SendMessageDto;
import com.chatbot.controllers.dto.response.ChatDto;
import com.chatbot.controllers.dto.response.MessageDto;
import com.chatbot.entities.Chat;
import com.chatbot.entities.Message;
import com.chatbot.mappers.EntityMapper;
import com.chatbot.repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final EntityMapper entityMapper;

    private final MessageService messageService;

    private final BotService botService;

    private final UserProfileService userProfileService;

    public Page<ChatDto> getChats(UUID userId, Pageable pageable) {
        return chatRepository.findAllByUserId(userId, pageable).map(entityMapper::toChatDto);
    }

    public Optional<Chat> isExist(UUID chatId, UUID userId) {
        return chatRepository.findByIdAndUserProfile_Id(chatId, userId);
    }

    @Transactional
    public MessageDto sendMessage(SendMessageDto sendMessageDto, UUID userId, UUID chatId) {
        Chat chat = null;
        if (chatId != null)
            chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));

        var message = new Message();
        var botAnswer = new Message();

        message.setText(sendMessageDto.message());
        if (chat != null) {
            message.setChat(chat);
            botAnswer.setChat(chat);
        } else {
            var newChat = new Chat();

            newChat.setName("Fixed name"); //TODO
            newChat.setUserProfile(userProfileService.getById(userId));
            chatRepository.save(newChat);

            message.setChat(newChat);
            botAnswer.setChat(newChat);
        }
        messageService.createMessage(message);

        botAnswer.setText(botService.answer(sendMessageDto.message()));
        return messageService.createMessage(botAnswer);
    }

    @Transactional
    public void deleteById(UUID chatId) {
        chatRepository.deleteById(chatId);
    }

}
