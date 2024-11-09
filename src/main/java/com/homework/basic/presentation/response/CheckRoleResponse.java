package com.homework.basic.presentation.response;

import com.homework.basic.domain.entity.UserRole;
import java.util.Set;

public record CheckRoleResponse(String nickname, Set<UserRole> roles) {
  public static CheckRoleResponse of(String nickname, Set<UserRole> roles) {
    return new CheckRoleResponse(nickname, roles);
  }
}
