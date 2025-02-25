package com.chatbot.handlers;

import com.chatbot.exceptions.global.ExceptionConfiguration;
import com.chatbot.exceptions.global.InternalException;
import com.chatbot.handlers.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<?> handleTransportException(RuntimeException e) {
        var annotation = e.getClass().getAnnotation(ExceptionConfiguration.class);

        return Objects.isNull(annotation) ? handleOtherExceptions(e) : getObjectResponseEntity(annotation);
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<?> handleOtherExceptions(Throwable e) {
        log.error("Handled exception: ", e);
        var internalException = new InternalException();
        var annotation = internalException.getClass().getAnnotation(ExceptionConfiguration.class);

        return getObjectResponseEntity(annotation);
    }

    private ResponseEntity<?> getObjectResponseEntity(ExceptionConfiguration annotation) {
        return ResponseEntity.status(annotation.status())
                .body(new ErrorResponse(
                        annotation.status().value(),
                        annotation.code(),
                        annotation.description(),
                        LocalDateTime.now())
                );
    }
}
