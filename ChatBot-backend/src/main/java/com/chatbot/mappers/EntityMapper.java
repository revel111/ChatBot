package com.chatbot.mappers;

import com.chatbot.controllers.dto.request.SignUpRequestDto;
import com.chatbot.entities.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EntityMapper {
    @Mapping(target = "id", ignore = true)
    UserProfile toUserProfile(SignUpRequestDto signUpRequestDto);
}
