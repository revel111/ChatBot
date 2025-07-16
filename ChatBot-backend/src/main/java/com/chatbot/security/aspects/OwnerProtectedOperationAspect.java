package com.chatbot.security.aspects;

import com.chatbot.security.contexts.AuthContextHolder;
import com.chatbot.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@RequiredArgsConstructor
public class OwnerProtectedOperationAspect {

    private final ChatService chatService;

    @Before("@annotation(com.chatbot.security.annotations.OwnerProtectedOperation)")
    public void checkOwnership(JoinPoint joinPoint) {
        var authContext = AuthContextHolder.get();

        if (authContext != null && authContext.id() != null && authContext.selectedChat() != null) {
            chatService.isExist(authContext.selectedChat(), authContext.id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized operation"));
        }
    }
}
