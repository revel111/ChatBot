package com.chatbot.services;

import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {

    private final ChatModel chatModel;

    public String answer(String message) {
        return chatModel.chat(message);
    }
}
