package com.chatbot.exceptions;

import com.chatbot.exceptions.global.ExceptionConfiguration;
import org.springframework.http.HttpStatus;

@ExceptionConfiguration(code = "USER_PROFILE_NOT_FOUND", status = HttpStatus.NOT_FOUND)
public class UserProfileNotFoundException extends RuntimeException {
}
