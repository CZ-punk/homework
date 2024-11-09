package com.homework.basic.presentation.error;

import com.homework.basic.presentation.response.ResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userExceptionHandle(UserException ex) {
        log.error("http status = {}, message = {}", ex.getHttpStatus(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(new ResponseDto<>(null, ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleSignatureException(AuthenticationException ex) {
        log.error("http status = {}, message = {}", HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto<>(null, ex.getMessage()));
    }
}