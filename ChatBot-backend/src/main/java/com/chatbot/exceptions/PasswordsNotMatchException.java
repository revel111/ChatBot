package com.chatbot.exceptions;

import com.chatbot.exceptions.global.ExceptionConfiguration;
import org.springframework.http.HttpStatus;

@ExceptionConfiguration(code = "PASSWORDS_NOT_MATCH_EXCEPTION", status = HttpStatus.BAD_REQUEST)
public class PasswordsNotMatchException extends RuntimeException {
}
