package com.chatbot.exceptions;

import com.chatbot.exceptions.global.ExceptionConfiguration;
import org.springframework.http.HttpStatus;

@ExceptionConfiguration(code = "UNAUTHORIZED", status = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
}
