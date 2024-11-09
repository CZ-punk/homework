package com.homework.basic.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "p_users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;
  private String phoneNumber;

  @Enumerated(value = EnumType.STRING)
  private UserRole userRole;

  public static User toUser(String username, String encodedPassword, String phoneNumber) {
    return User.builder()
        .username(username)
        .password(encodedPassword)
        .phoneNumber(phoneNumber)
        .userRole(UserRole.USER)
        .build();
  }

}
