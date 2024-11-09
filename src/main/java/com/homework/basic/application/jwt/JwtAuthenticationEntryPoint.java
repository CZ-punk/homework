package com.homework.basic.application.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@Slf4j(topic = "jwt authentication entry point")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final HandlerExceptionResolver resolver;

  public JwtAuthenticationEntryPoint(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) {
    log.error("entry point start.");
    resolver.resolveException(request, response, null, authException);
    log.error("entry point end.");
  }
}
