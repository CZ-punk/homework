package com.homework.basic.presentation.response;

import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.entity.UserRole;

public record UserInfoDto(String username, String phoneNumber, UserRole userRole) {

  public static UserInfoDto of(User user) {
    return new UserInfoDto(user.getUsername(), user.getPhoneNumber(), user.getUserRole());
  }
}
