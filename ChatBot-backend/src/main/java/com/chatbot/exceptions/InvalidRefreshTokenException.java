package com.chatbot.exceptions;

import com.chatbot.exceptions.global.ExceptionConfiguration;
import org.springframework.http.HttpStatus;

@ExceptionConfiguration(code = "INVALID_REFRESH_TOKEN", status = HttpStatus.BAD_REQUEST)
public class InvalidRefreshTokenException extends RuntimeException {
}
