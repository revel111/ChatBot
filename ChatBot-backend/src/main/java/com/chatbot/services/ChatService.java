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
import org.springframework.stereotype.Service;

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

    public boolean isExist(UUID chatId, UUID userId) {
        return chatRepository.existsByIdAndUserProfileId(chatId, userId);
    }

    public MessageDto sendMessageAndCreateChatIfNotExist(SendMessageDto sendMessageDto, UUID userId) {
        Optional<Chat> chat = Optional.empty();
        if (sendMessageDto.chatId() != null)
            chat = chatRepository.findById(sendMessageDto.chatId());

        var message = new Message();
        var botAnswer = new Message();

        message.setText(sendMessageDto.message());
        if (chat.isPresent()) {
            message.setChat(chat.get());
            botAnswer.setChat(chat.get());
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

    public void deleteById(UUID chatId) {
        chatRepository.deleteById(chatId);
    }

}
