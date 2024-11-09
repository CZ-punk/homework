package com.homework.basic.presentation.response;

import com.homework.basic.domain.entity.UserRole;
import java.util.Set;

public record SignupResponse(String username, String nickname, Set<UserRole> roles) {

  public static SignupResponse of(String username, String nickname, Set<UserRole> roles) {
    return new SignupResponse(username, nickname, roles);
  }
}
