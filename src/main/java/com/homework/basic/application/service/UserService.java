package com.homework.basic.application.service;

import com.homework.basic.application.jwt.JwtUtils;
import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.repository.UserRepository;
import com.homework.basic.presentation.error.UserException;
import com.homework.basic.presentation.request.LoginRequest;
import com.homework.basic.presentation.request.SignupRequest;
import com.homework.basic.presentation.response.CheckRoleResponse;
import com.homework.basic.presentation.response.LoginResponse;
import com.homework.basic.presentation.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;

  @Transactional
  public SignupResponse signup(SignupRequest signupDto) {
    User user = userRepository.findByUsername(signupDto.username()).orElse(null);
    if (user != null) throw new UserException(HttpStatus.BAD_REQUEST, "duplication username.");
    String encodedPassword = passwordEncoder.encode(signupDto.password());

    User saveUser =
        userRepository.save(
            User.toUser(signupDto.username(), encodedPassword, signupDto.nickname()));
    return SignupResponse.of(saveUser.getUsername(), saveUser.getNickname(), saveUser.getRoles());
  }

  @Transactional(readOnly = true)
  public LoginResponse sign(LoginRequest loginDto) {
    User user =
        userRepository
            .findByUsername(loginDto.username())
            .orElseThrow(
                () ->
                    new UserException(
                        HttpStatus.NOT_FOUND,
                        "not found user by username = " + loginDto.username()));
    if (!passwordEncoder.matches(loginDto.password(), user.getPassword()))
      throw new UserException(HttpStatus.UNAUTHORIZED, "not matching password.");

    return LoginResponse.of(jwtUtils.createToken(user.getUsername(), user.getRoles()));
  }

  @Transactional(readOnly = true)
  public CheckRoleResponse accessUser(User user) {
    return CheckRoleResponse.of(user.getNickname(), user.getRoles());
  }

  @Transactional(readOnly = true)
  public CheckRoleResponse accessAdmin(User user) {
    return CheckRoleResponse.of(user.getNickname(), user.getRoles());
  }

  public CheckRoleResponse checkRoles(User user) {
    return CheckRoleResponse.of(user.getNickname(), user.getRoles());
  }
}
