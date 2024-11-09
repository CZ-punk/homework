package com.homework.basic.application.jwt;

import com.homework.basic.application.service.CacheService;
import com.homework.basic.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "user details service")
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final CacheService cacheService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("create loadUserByUsername start");
    User user = cacheService.getUser(username);
    log.info("create loadUserByUsername end");
    return new UserDetailsImpl(user);
  }
}
