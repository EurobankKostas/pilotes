package com.tui.proof.domain.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PilotesOrderException extends Exception {

    private final HttpStatus status;

    public PilotesOrderException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public PilotesOrderException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
