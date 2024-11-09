package com.homework.basic.presentation.error;

import com.homework.basic.presentation.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ResponseDto<>(null, ex.getMessage()));
  }
}
