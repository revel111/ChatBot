package com.chatbot.exceptions;

import com.chatbot.exceptions.global.ExceptionConfiguration;
import org.springframework.http.HttpStatus;

@ExceptionConfiguration(code = "USER_NOT_FOUND_EXCEPTION", status = HttpStatus.UNAUTHORIZED)
public class UserNotFoundException extends RuntimeException {
}
