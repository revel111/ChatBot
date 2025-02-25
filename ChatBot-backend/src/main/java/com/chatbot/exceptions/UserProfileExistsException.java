package com.chatbot.exceptions;

import com.chatbot.exceptions.global.ExceptionConfiguration;
import org.springframework.http.HttpStatus;

@ExceptionConfiguration(code = "USER_PROFILE_EXISTS", status = HttpStatus.CONFLICT)
public class UserProfileExistsException extends RuntimeException {
}
