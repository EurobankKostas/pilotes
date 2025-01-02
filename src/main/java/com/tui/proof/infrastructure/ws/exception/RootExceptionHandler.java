package com.tui.proof.infrastructure.ws.exception;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tui.proof.domain.exception.PilotesOrderException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
public class RootExceptionHandler {

    @ExceptionHandler({PilotesOrderException.class})
    public ResponseEntity<ProblemDetail> handlePilotesOrderException(PilotesOrderException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
            ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage())
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                Arrays.stream(ex.getBindingResult().getFieldErrors().toArray(new FieldError[0]))
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce("", (a, b) -> a + (StringUtils.isBlank(b) ? "" : " | " + b ))
            )
        );
    }
}
