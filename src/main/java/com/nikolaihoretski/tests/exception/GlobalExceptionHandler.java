package com.nikolaihoretski.tests.exception;

import com.nikolaihoretski.tests.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceError.class)
    public ResponseEntity<ErrorResponseDto> handleBaseException(@NonNull ServiceError exception, @NonNull HttpServletRequest request) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                exception.getErrorCode(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponseDto, exception.getStatus());
    }

}
