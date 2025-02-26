package com.chatbot.security.aspects;

import com.chatbot.controllers.dto.request.SendMessageDto;
import com.chatbot.exceptions.UnauthorizedException;
import com.chatbot.security.contexts.AuthContextHolder;
import com.chatbot.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class OwnerProtectedOperationAspect {

    private final ChatService chatService;

    @Before("@annotation(com.chatbot.security.annotations.OwnerProtectedOperation)")
    public void checkOwnership(JoinPoint joinPoint) {
        var userId = AuthContextHolder.get().id();

        Object[] args = joinPoint.getArgs();
        UUID chatId = null;

        for (Object arg : args)
            if (arg instanceof UUID) {
                chatId = (UUID) arg;
                break;
            } else if (arg instanceof SendMessageDto sendMessageDto) {
                chatId = sendMessageDto.chatId();
                break;
            }

        if (chatId != null && !chatService.isExist(chatId, userId))
            throw new UnauthorizedException();
    }
}
