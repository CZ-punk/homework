package com.homework.basic.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.homework.basic.application.service.UserService;
import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.entity.UserRole;
import com.homework.basic.domain.repository.UserRepository;
import com.homework.basic.presentation.error.UserException;
import com.homework.basic.presentation.request.LoginRequest;
import com.homework.basic.presentation.request.SignupRequest;
import com.homework.basic.presentation.response.CheckRoleResponse;
import com.homework.basic.presentation.response.LoginResponse;
import com.homework.basic.presentation.response.SignupResponse;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

  @Autowired private UserService userService;
  private static UserRepository staticUserRepository;

  @BeforeAll
  public static void setUp(@Autowired UserRepository userRepository) {
    staticUserRepository = userRepository;
  }

  @AfterAll
  public static void tearDown() {
    if (staticUserRepository != null) {
      staticUserRepository.deleteAll(); // 모든 데이터 삭제
    }
  }

  @Test
  @DisplayName("회원가입")
  public void signup() {
    // given
    SignupRequest requestDto = new SignupRequest("user", "password", "nickname");

    // when
    SignupResponse responseDto = userService.signup(requestDto);

    // then
    assertThat(responseDto.username()).isEqualTo("user");
    assertThat(responseDto.nickname()).isEqualTo("nickname");
    assertThat(responseDto.roles().contains(UserRole.USER)).isTrue();
  }

  @Test
  @DisplayName("로그인")
  public void sign() {
    // given
    LoginRequest requestDto = new LoginRequest("user", "password");

    // when
    LoginResponse responseDto = userService.sign(requestDto);

    // then
    assertThat(responseDto.token()).isNotNull();
    assertThat(responseDto.token()).isInstanceOf(String.class);
  }

  @Test
  @DisplayName("룰 체크 - ROLE_USER")
  public void RoleCheck_USER() {
    // given
    User user =
        staticUserRepository
            .findByUsernameWithRoles("user")
            .orElseThrow(() -> new UserException(HttpStatus.UNAUTHORIZED, "unauthorized."));

    // when
    CheckRoleResponse responseDto = userService.accessUser(user);

    // then
    assertThat(responseDto.nickname()).isEqualTo("nickname");
    assertThat(responseDto.roles().contains(UserRole.USER)).isTrue();
  }

  @Test
  @DisplayName("룰 체크 - ROLE_ADMIN")
  public void RoleCheck_ADMIN() {
    // given
    Set<UserRole> roles = new HashSet<>();
    roles.add(UserRole.ADMIN);
    User user = new User(99L, "admin", "password", "nickname", roles);

    // when
    CheckRoleResponse responseDto = userService.accessAdmin(user);

    // then
    assertThat(responseDto.nickname()).isEqualTo("nickname");
    assertThat(responseDto.roles().contains(UserRole.ADMIN)).isTrue();
  }
}
