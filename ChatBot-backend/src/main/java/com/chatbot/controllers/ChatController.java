package com.chatbot.controllers;

import com.chatbot.controllers.dto.request.SendMessageDto;
import com.chatbot.controllers.dto.response.ChatDto;
import com.chatbot.controllers.dto.response.MessageDto;
import com.chatbot.security.annotations.OwnerProtectedOperation;
import com.chatbot.security.contexts.AuthContextHolder;
import com.chatbot.services.ChatService;
import com.chatbot.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    private final MessageService messageService;

    @GetMapping("/me")
    public ResponseEntity<Page<ChatDto>> getChats(@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        var userId = AuthContextHolder.get().id();
        return ResponseEntity.ok(chatService.getChats(userId, pageable));
    }

    @PostMapping
    @OwnerProtectedOperation
    public ResponseEntity<MessageDto> sendMessageAndCreateChatIfNotExist(@Valid @RequestBody SendMessageDto sendMessageDto) {
        var userId = AuthContextHolder.get().id();
        return ResponseEntity.ok(chatService.sendMessageAndCreateChatIfNotExist(sendMessageDto, userId));
    }

    @GetMapping("/{chatId}/messages")
    @OwnerProtectedOperation
    public ResponseEntity<Page<MessageDto>> getMessagesByChatId(@PathVariable UUID chatId,
                                                                @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(messageService.getMessagesByChatId(chatId, pageable));
    }

    @DeleteMapping("/{chatId}")
    @OwnerProtectedOperation
    public ResponseEntity<Void> deleteById(@PathVariable UUID chatId) {
        chatService.deleteById(chatId);
        return ResponseEntity.ok(null);
    }
}
