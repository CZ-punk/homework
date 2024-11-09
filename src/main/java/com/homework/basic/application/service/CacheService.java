package com.homework.basic.application.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.repository.UserRepository;
import com.homework.basic.presentation.error.UserException;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

  private final Cache<String, User> userCache;
  private final UserRepository userRepository;

  public CacheService(UserRepository userRepository) {
    this.userRepository = userRepository;
    userCache =
        Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(100).build();
  }

  public User getUser(String username) {
    return userCache.get(username, id -> loadUserFromDatabase(username));
  }

  private User loadUserFromDatabase(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(
            () ->
                new UserException(
                    HttpStatus.NOT_FOUND, "not found username by username = " + username));
  }

  public void invalidateUser(String username) {
    userCache.invalidate(username);
  }
}
