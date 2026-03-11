package com.jeari.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Use BAD_REQUEST for invalid state
public class RecruitmentNotOpenException extends RuntimeException {
    public RecruitmentNotOpenException(String message) {
        super(message);
    }
}
