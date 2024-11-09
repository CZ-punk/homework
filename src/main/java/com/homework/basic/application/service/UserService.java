package com.homework.basic.application.service;

import com.homework.basic.application.jwt.JwtUtils;
import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.repository.UserRepository;
import com.homework.basic.presentation.error.UserException;
import com.homework.basic.presentation.request.LoginDto;
import com.homework.basic.presentation.request.SignupDto;
import com.homework.basic.presentation.response.ResponseDto;
import com.homework.basic.presentation.response.UserInfoDto;
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
  public ResponseDto<?> signup(SignupDto signupDto) {
    User user = userRepository.findByUsername(signupDto.username()).orElse(null);
    if (user != null) throw new UserException(HttpStatus.BAD_REQUEST, "duplication username.");
    String encodedPassword = passwordEncoder.encode(signupDto.password());

    userRepository.save(
        User.toUser(signupDto.username(), encodedPassword, signupDto.phoneNumber()));
    return new ResponseDto<>("success signup.", null);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> sign(LoginDto loginDto) {
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

    String token = jwtUtils.createToken(user.getUsername(), user.getUserRole());
    return new ResponseDto<>(token, null);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> accessAdmin(User user) {
    return new ResponseDto<>(UserInfoDto.of(user), null);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> accessUser(User user) {
    return new ResponseDto<>(UserInfoDto.of(user), null);
  }
}
