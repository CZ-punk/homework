package com.homework.basic.presentation.response;

public record LoginResponse(String token) {
  public static LoginResponse of(String token) {
    return new LoginResponse(token);
  }
}
