package com.chatbot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {

//    private final ChatLanguageModel chatLanguageModel;

    public String answer(String message) {
//        return chatLanguageModel.chat(message);
        return "answer";
    }
}
