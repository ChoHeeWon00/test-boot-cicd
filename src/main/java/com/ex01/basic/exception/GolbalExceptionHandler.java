package com.ex01.basic.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GolbalExceptionHandler {
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> memberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body( null );
    }
    @ExceptionHandler(MemberDuplicateException.class)
    public ResponseEntity<String> memberDuplicateException(MemberDuplicateException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body( e.getMessage() );
    }
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<Boolean> invalidLoginException(InvalidLoginException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body( false );
    }
}
