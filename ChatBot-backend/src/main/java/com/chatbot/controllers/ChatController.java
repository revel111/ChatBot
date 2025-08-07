package com.chatbot.controllers;

import com.chatbot.controllers.dto.request.SendMessageDto;
import com.chatbot.controllers.dto.response.ChatDto;
import com.chatbot.controllers.dto.response.MessageDto;
import com.chatbot.security.annotations.OwnerProtectedOperation;
import com.chatbot.security.contexts.AuthContextHolder;
import com.chatbot.services.ChatService;
import com.chatbot.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Chat management")
public class ChatController {

    private final ChatService chatService;

    private final MessageService messageService;

    @GetMapping("/me")
    @Operation(
            description = "Get all chats for the authenticated user.",
            summary = "Get User Chats",
            responses = {
                    @ApiResponse(
                            description = "Successful retrieval of chats",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized access",
                            responseCode = "401"
                    )
            },
            parameters = @Parameter(
                    name = "pageable",
                    in = ParameterIn.QUERY,
                    description = "Pagination parameters, e.g., page, size, sort",
                    schema = @Schema(type = "object")
            )
    )
    public ResponseEntity<Page<ChatDto>> getChats(@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        var userId = AuthContextHolder.get().id();
        return ResponseEntity.ok(chatService.getChats(userId, pageable));
    }

    @PostMapping
    @OwnerProtectedOperation
    @Operation(
            description = "Send a message in the selected chat. The chat must be selected by the user.",
            summary = "Send Message",
            responses = {
                    @ApiResponse(
                            description = "Message sent successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Chat not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized access",
                            responseCode = "401"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SendMessageDto containing the message text and optional chat ID.",
                    required = true,
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SendMessageDto.class)
                            )
                    }
            ),
            parameters = @Parameter(
                    name = "x-chat-id",
                    in = ParameterIn.HEADER,
                    description = "The ID of the chat to send the message to. If not provided, a new chat will be created.",
                    schema = @Schema(type = "string", format = "uuid")
            )
    )
    public ResponseEntity<MessageDto> sendMessage(@Valid @RequestBody SendMessageDto sendMessageDto) {
        var context = AuthContextHolder.get();
        return ResponseEntity.ok(chatService.sendMessage(sendMessageDto, context.id(), context.selectedChat()));
    }

    @GetMapping("/{chatId}/messages")
    @OwnerProtectedOperation
    @Operation(
            description = "Get messages by chat ID. The chat must be selected by the user.",
            summary = "Get Messages by Chat ID",
            responses = {
                    @ApiResponse(
                            description = "Messages retrieved successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Chat not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized access",
                            responseCode = "401"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "chatId",
                            in = ParameterIn.PATH,
                            description = "The ID of the chat to retrieve messages from",
                            required = true,
                            schema = @Schema(type = "string", format = "uuid")
                    ),
                    @Parameter(
                            name = "pageable",
                            in = ParameterIn.QUERY,
                            description = "Pagination parameters, e.g., page, size, sort",
                            schema = @Schema(type = "object")
                    )
            }
    )
    public ResponseEntity<Page<MessageDto>> getMessagesByChatId(@PathVariable UUID chatId,
                                                                @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(messageService.getMessagesByChatId(chatId, pageable));
    }

    @DeleteMapping("/{chatId}")
    @OwnerProtectedOperation
    @Operation(
            description = "Delete a chat by its ID. The chat must be selected by the user.",
            summary = "Delete Chat by ID",
            responses = {
                    @ApiResponse(
                            description = "Chat deleted successfully",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Chat not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized access",
                            responseCode = "401"
                    )
            },
            parameters = @Parameter(
                    name = "chatId",
                    in = ParameterIn.PATH,
                    description = "The ID of the chat to delete",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
    )
    public ResponseEntity<Void> deleteById(@PathVariable UUID chatId) {
        chatService.deleteById(chatId);
        return ResponseEntity.noContent().build();
    }
}
