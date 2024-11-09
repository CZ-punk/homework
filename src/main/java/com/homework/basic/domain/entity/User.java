package com.homework.basic.domain.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
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
  private String nickname;

  @ElementCollection(targetClass = UserRole.class)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "username"))
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Set<UserRole> roles = new HashSet<>();

  public static User toUser(String username, String encodedPassword, String nickname) {
    Set<UserRole> set = new HashSet<>();
    set.add(UserRole.USER);
    return User.builder()
        .username(username)
        .password(encodedPassword)
        .nickname(nickname)
        .roles(set)
        .build();
  }
}
