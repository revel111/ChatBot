package com.chatbot.mappers;

import com.chatbot.controllers.dto.request.SignUpRequestDto;
import com.chatbot.controllers.dto.response.ChatDto;
import com.chatbot.controllers.dto.response.MessageDto;
import com.chatbot.controllers.dto.response.UserProfileDto;
import com.chatbot.entities.Chat;
import com.chatbot.entities.Message;
import com.chatbot.entities.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chats", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserProfile toUserProfile(SignUpRequestDto signUpRequestDto);

    UserProfileDto toUserProfileDto(UserProfile userProfile);

    ChatDto toChatDto(Chat chat);

    @Mapping(target = "chatId", source = "chat.id")
    MessageDto toMessageDto(Message message);
}
