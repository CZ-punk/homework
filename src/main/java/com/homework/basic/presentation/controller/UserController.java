package com.homework.basic.presentation.controller;

import com.homework.basic.application.jwt.UserDetailsImpl;
import com.homework.basic.application.service.UserService;
import com.homework.basic.presentation.request.LoginRequest;
import com.homework.basic.presentation.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j(topic = "user controller")
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody SignupRequest signupDto) {
    return ResponseEntity.ok(userService.signup(signupDto));
  }

  @PostMapping("/sign")
  public ResponseEntity<?> sign(@RequestBody LoginRequest loginDto) {
    return ResponseEntity.ok(userService.sign(loginDto));
  }

  @GetMapping
  public ResponseEntity<?> checkRoles(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(userService.checkRoles(userDetails.getUser()));
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user")
  public ResponseEntity<?> accessUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(userService.accessUser(userDetails.getUser()));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  public ResponseEntity<?> accessAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(userService.accessAdmin(userDetails.getUser()));
  }
}
