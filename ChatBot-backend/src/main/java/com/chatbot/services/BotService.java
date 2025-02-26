package com.chatbot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {

    public String answer(String message) {
        return "answer";
    }

}
